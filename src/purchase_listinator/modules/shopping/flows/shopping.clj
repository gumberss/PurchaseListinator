(ns purchase-listinator.modules.shopping.flows.shopping
  (:require
    [purchase-listinator.modules.shopping.schemas.models.shopping-list :as models.internal.shopping-list]
    [schema.core :as s]
    [cats.monad.either :refer [left]]
    [clojure.core.async :refer [go <!! <!] :as async]
    [purchase-listinator.misc.either :as either]
    [purchase-listinator.modules.shopping.schemas.models.shopping-initiation :as models.internal.shopping-initiation]
    [purchase-listinator.modules.shopping.diplomat.db.datomic.shopping :as datomic.shopping]
    [purchase-listinator.modules.shopping.logic.shopping :as logic.shopping]
    [purchase-listinator.modules.shopping.logic.shopping-location :as logic.shopping-location]
    [purchase-listinator.misc.date :as misc.date]
    [purchase-listinator.modules.shopping.diplomat.db.mongo.shopping-location :as mongo.shopping-location]
    [purchase-listinator.misc.general :as misc.general]
    [purchase-listinator.modules.shopping.schemas.models.shopping-initiation-data-request :as models.internal.shopping-initiation-data-request]
    [purchase-listinator.modules.shopping.logic.shopping-cart-event :as logic.shopping-cart-event]
    [purchase-listinator.components.http :as components.http]
    [purchase-listinator.modules.shopping.logic.shopping-cart :as logic.shopping-cart]
    [purchase-listinator.modules.shopping.logic.shopping-category :as logic.shopping-category]
    [purchase-listinator.modules.shopping.diplomat.publishers.shopping :as publishers.shopping]
    [purchase-listinator.modules.shopping.diplomat.http.client :as http.client.shopping]))

(s/defn init-shopping
  [{shopping-id      :id
    purchase-list-id :list-id
    :as              shopping-initiation} :- models.internal.shopping-initiation/ShoppingInitiation
   user-id :- s/Uuid
   {:shopping/keys [main-db mongo http]}]
  (either/try-right
    (let [now (misc.date/numb-now)
          shopping (-> (logic.shopping/initiation->shopping shopping-initiation now)
                       (logic.shopping/link-with-user user-id))]
      (->> [(go (http.client.shopping/init-shopping-cart shopping-id purchase-list-id user-id http))
            (go (-> (logic.shopping-location/initiation->shopping-location shopping-initiation (misc.general/squuid))
                    (mongo.shopping-location/upsert mongo)))
            (go (datomic.shopping/upsert shopping main-db))]
           (async/map vector)
           <!!
           last))))

(s/defn get-initial-data
  [{:keys [latitude longitude]} :- models.internal.shopping-initiation-data-request/ShoppingInitiationDataRequest
   user-id :- s/Uuid
   {:shopping/keys [mongo main-db http]}]
  (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)
        near-places (mongo.shopping-location/find-by-location latitude longitude mongo)
        first-near-shopping (and (seq near-places)
                                 (datomic.shopping/get-by-id (-> near-places first :shopping-id) allowed-lists-ids main-db))]
    (if first-near-shopping
      first-near-shopping
      (left {:status 404 :data "not-found"}))))

(s/defn cart-module-management :- models.internal.shopping-list/ShoppingList
  [list-id :- s/Uuid
   user-id :- s/Uuid
   shopping-id :- s/Uuid
   http :- components.http/IHttp]
  (let [{:keys [purchase-list] :as new-cart} (http.client.shopping/get-shopping-cart list-id shopping-id user-id http)
        shopping-cart (logic.shopping-cart/->shopping-cart shopping-id new-cart)
        shopping-list (logic.shopping/purchase-list->shopping-list shopping-id purchase-list)]
    (logic.shopping-cart-event/apply-cart shopping-cart shopping-list)))

(s/defn get-in-progress-list
  [shopping-id :- s/Uuid
   user-id :- s/Uuid
   {:shopping/keys [main-db http]}]
  (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)
        {:keys [list-id]} (datomic.shopping/get-by-id shopping-id allowed-lists-ids main-db)
        shopping-module-completed (cart-module-management list-id user-id shopping-id http)]
    shopping-module-completed))

(s/defn active-shopping
  [list-id :- s/Uuid
   user-id :- s/Uuid
   {:shopping/keys [main-db http]}]
  (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)]
    (datomic.shopping/get-in-progress-by-list-id list-id allowed-lists-ids main-db)))

(s/defn finished-shopping
  [user-id :- s/Uuid
   {:keys [list-id id] :as shopping}
   http :- components.http/IHttp]
  (let [{:keys [purchase-list] :as new-cart} (http.client.shopping/get-shopping-exclusive-cart list-id id user-id http)
        new-shopping-cart (logic.shopping-cart/->shopping-cart id new-cart)
        shopping-list (logic.shopping/purchase-list->shopping-list id purchase-list)
        shopping (->> (logic.shopping-cart-event/apply-cart new-shopping-cart shopping-list)
                      :categories
                      (map (partial logic.shopping-category/->shopping-category id))
                      (logic.shopping/fill-items-empty-quantity-in-cart)
                      (logic.shopping/fill-shopping-categories shopping)
                      (logic.shopping/finish))]
    shopping))

(s/defn finish
  [shopping-id :- s/Uuid
   user-id :- s/Uuid
   {:shopping/keys [main-db rabbitmq-channel http]}]
  (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)
        shopping (datomic.shopping/get-by-id shopping-id allowed-lists-ids main-db)
        shopping (finished-shopping user-id shopping http)]
    (datomic.shopping/upsert shopping main-db)
    (publishers.shopping/shopping-finished shopping rabbitmq-channel)))

(s/defn mark-items
  [request-id :- s/Uuid
   shopping-id :- s/Uuid
   user-id :- s/Uuid
   image :- s/Str
   {:shopping/keys [http main-db]}]
  (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)
        {:keys [list-id]} (datomic.shopping/get-by-id shopping-id allowed-lists-ids main-db)
        shopping (cart-module-management list-id user-id shopping-id http)
        item-ids (http.client.shopping/post-interaction request-id shopping image user-id http)
        items-marked-by-ia (logic.shopping/get-items shopping item-ids)
        now (misc.date/numb-now)
        _response (http.client.shopping/post-cart-events-in-batch shopping items-marked-by-ia now user-id http)
        final-shopping (cart-module-management list-id user-id shopping-id http)]
    final-shopping))