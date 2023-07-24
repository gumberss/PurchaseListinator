(ns purchase-listinator.flows.shopping
  (:require
    [purchase-listinator.models.internal.shopping-list :as models.internal.shopping-list]
    [purchase-listinator.wires.in.shopping-cart :as wires.in.shopping-cart]
    [schema.core :as s]
    [cats.monad.either :refer [left]]
    [clojure.core.async :refer [go <!! <!] :as async]
    [purchase-listinator.misc.either :as either]
    [purchase-listinator.models.internal.shopping-initiation :as models.internal.shopping-initiation]
    [purchase-listinator.dbs.datomic.shopping :as datomic.shopping]
    [purchase-listinator.components.http :as components.http]
    [purchase-listinator.logic.shopping :as logic.shopping]
    [purchase-listinator.logic.shopping-location :as logic.shopping-location]
    [purchase-listinator.misc.date :as misc.date]
    [purchase-listinator.dbs.mongo.shopping-location :as mongo.shopping-location]
    [purchase-listinator.misc.general :as misc.general]
    [purchase-listinator.logic.errors :as logic.errors]
    [purchase-listinator.models.internal.shopping-initiation-data-request :as models.internal.shopping-initiation-data-request]
    [purchase-listinator.dbs.datomic.purchase-list :as dbs.datomic.purchase-list]
    [purchase-listinator.logic.shopping-cart-event :as logic.shopping-cart-event]
    [purchase-listinator.dbs.redis.shopping-cart :as redis.shopping-cart]
    [purchase-listinator.logic.shopping-cart :as logic.shopping-cart]
    [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
    [purchase-listinator.dbs.datomic.shopping-event :as dbs.datomic.shopping-events]
    [purchase-listinator.logic.shopping-category :as logic.shopping-category]
    [purchase-listinator.dbs.redis.shopping-cart :as dbs.redis.shopping-cart]
    [purchase-listinator.publishers.shopping :as publishers.shopping]
    [purchase-listinator.endpoints.http.client.shopping :as http.client.shopping]
    [purchase-listinator.logic.price-suggestion :as logic.price-suggestion]))

(s/defn init-shopping
  [{shopping-id      :id
    purchase-list-id :list-id
    :as              shopping-initiation} :- models.internal.shopping-initiation/ShoppingInitiation
   user-id :- s/Uuid
   {:keys [datomic mongo redis http]}]
  (either/try-right
    (let [now (misc.date/numb-now)
          {:keys [id] :as shopping} (-> (logic.shopping/initiation->shopping shopping-initiation now)
                                        (logic.shopping/link-with-user user-id))]
      (->> [(go (http.client.shopping/init-shopping-cart shopping-id purchase-list-id user-id http))
            (go (-> (logic.shopping-cart/init id)
                    (redis.shopping-cart/init-cart redis)))
            (go (-> (logic.shopping-location/initiation->shopping-location shopping-initiation (misc.general/squuid))
                    (mongo.shopping-location/upsert mongo)))
            (go (datomic.shopping/upsert shopping datomic))]
           (async/map vector)
           <!!
           last))))

(s/defn get-initial-data
  [{:keys [latitude longitude]} :- models.internal.shopping-initiation-data-request/ShoppingInitiationDataRequest
   user-id :- s/Uuid
   {:keys [mongo datomic http]}]
  (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)
        near-places (mongo.shopping-location/find-by-location latitude longitude mongo)
        first-near-shopping (and (seq near-places)
                                 (datomic.shopping/get-by-id (-> near-places first :shopping-id) allowed-lists-ids datomic))]
    (if first-near-shopping
      first-near-shopping
      (left {:status 404 :data "not-found"}))))

(s/defn generate-price-suggestion-events! :- models.internal.shopping-list/ShoppingList
  [items-without-price-ids :- [s/Uuid]
   user-id :- s/Uuid
   shopping :- models.internal.shopping-list/ShoppingList
   cart :- models.internal.shopping-cart/Cart
   {:keys [http redis]}]
  (let [cart+price-suggestions (->> (http.client.shopping/get-price-suggestion items-without-price-ids user-id http)
                                    :price-suggestion
                                    (map (partial logic.price-suggestion/->cart-event (random-uuid) user-id shopping))
                                    (reduce logic.shopping-cart-event/add-event cart))]
    (-> (redis.shopping-cart/upsert cart+price-suggestions redis)
        (logic.shopping-cart-event/apply-cart shopping))))

(s/defn shadow-cart-management
  [list-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [shopping-id]} :- models.internal.shopping-list/ShoppingList
   actual-shopping-cart :- models.internal.shopping-list/ShoppingList
   http :- components.http/IHttp]
  (try
    (let [{:keys [purchase-list] :as new-cart} (http.client.shopping/get-shopping-cart list-id user-id http)
          new-shopping-cart (logic.shopping-cart/->cart shopping-id new-cart)
          shopping (logic.shopping/purchase-list->shopping-list shopping-id purchase-list)
          shopping+cart (logic.shopping-cart-event/apply-cart new-shopping-cart shopping)]
      (clojure.pprint/pprint "[[EQUALS]]: ")
      (clojure.pprint/pprint (= actual-shopping-cart shopping+cart))
      (clojure.pprint/pprint "[[DIFF]]: ")
      (clojure.pprint/pprint (logic.shopping-cart/compare-carts actual-shopping-cart shopping+cart)))
    (catch Exception ex
      (clojure.pprint/pprint "[[SHADOW-CART-MANAGEMENT]]")
      (clojure.pprint/pprint ex))))

(s/defn get-in-progress-list
  [shopping-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [datomic redis http] :as components}]
  (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)
        {:keys [list-id date]} (datomic.shopping/get-by-id shopping-id allowed-lists-ids datomic)
        purchase-list (dbs.datomic.purchase-list/get-management-data list-id allowed-lists-ids date datomic)
        shopping (logic.shopping/purchase-list->shopping-list shopping-id purchase-list)
        cart (redis.shopping-cart/find-cart shopping-id redis)
        shopping+cart (logic.shopping-cart-event/apply-cart cart shopping)
        without-price-items-ids (map :id (logic.shopping/items-without-prices shopping+cart))
        shopping-completed (if (seq without-price-items-ids)
                             (generate-price-suggestion-events! without-price-items-ids user-id shopping cart components)
                             shopping+cart)
        _ (shadow-cart-management list-id user-id shopping shopping-completed http)]
    shopping-completed))

(s/defn find-existent
  [list-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [datomic http]}]
  (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)]
    (if-let [existent (datomic.shopping/get-in-progress-by-list-id list-id allowed-lists-ids datomic)]
      existent
      (left (logic.errors/build 404 {:message nil})))))

(s/defn receive-cart-event
  [{:keys [shopping-id user-id] :as event} :- models.internal.shopping-cart/CartEvent
   wire :- wires.in.shopping-cart/ChangeItemEvent
   {:keys [redis datomic http]}]
  (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)
        list-id (datomic.shopping/get-list-id-by-shopping-id shopping-id allowed-lists-ids datomic)
        event (assoc event :purchase-list-id list-id)]
    (-> (redis.shopping-cart/find-cart shopping-id redis)
        (logic.shopping-cart-event/add-event event)
        (redis.shopping-cart/upsert redis))
    (http.client.shopping/send-shopping-cart-events (assoc wire :purchase-list-id list-id) user-id http)
    event))

(s/defn receive-cart-event-by-list
  [{:keys [purchase-list-id] :as event} :- models.internal.shopping-cart/CartEvent
   user-id :- s/Uuid
   {:keys [redis datomic http]}]
  (try (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)
             shopping-id (:id (datomic.shopping/get-in-progress-by-list-id purchase-list-id allowed-lists-ids datomic))
             event+shopping-id (assoc event :shopping-id shopping-id)]
         (some-> shopping-id
                 (redis.shopping-cart/find-cart redis)
                 (logic.shopping-cart-event/add-event event+shopping-id)
                 (redis.shopping-cart/upsert redis)))
       (catch Exception e
         (println e)
         (throw e)))
  event)

(s/defn receive-cart-event-by-category
  [{:keys [category-id user-id] :as event} :- models.internal.shopping-cart/CartEvent
   {:keys [redis datomic http]}]
  (try (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)
             shopping-id (:id (datomic.shopping/get-in-progress-by-category-id category-id allowed-lists-ids datomic))
             event+shopping-id (assoc event :shopping-id shopping-id)]
         (some-> shopping-id
                 (redis.shopping-cart/find-cart redis)
                 (logic.shopping-cart-event/add-event event+shopping-id)
                 (redis.shopping-cart/upsert redis)))
       (catch Exception e
         (println e)
         (throw e)))
  event)

(s/defn finish
  [shopping-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [redis datomic rabbitmq http]}]
  (let [allowed-lists-ids (http.client.shopping/get-allowed-lists user-id http)
        {:keys [events] :as cart} (redis.shopping-cart/find-cart shopping-id redis)
        {:keys [list-id date id] :as shopping} (datomic.shopping/get-by-id shopping-id allowed-lists-ids datomic)
        purchase-list (dbs.datomic.purchase-list/get-management-data list-id allowed-lists-ids date datomic)
        shopping-list (logic.shopping/purchase-list->shopping-list shopping-id purchase-list)
        shopping (->> (logic.shopping-cart-event/apply-cart cart shopping-list)
                      :categories
                      (map (partial logic.shopping-category/->shopping-category id))
                      (logic.shopping/fill-items-empty-quantity-in-cart)
                      (logic.shopping/fill-shopping-categories shopping)
                      (logic.shopping/finish))]
    (dbs.datomic.shopping-events/upsert events datomic)
    (datomic.shopping/upsert shopping datomic)
    (dbs.redis.shopping-cart/delete id redis)
    (publishers.shopping/shopping-finished shopping events rabbitmq)))

