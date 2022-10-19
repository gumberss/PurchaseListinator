(ns purchase-listinator.flows.shopping
  (:require [schema.core :as s]
            [cats.monad.either :refer [left]]
            [clojure.core.async :refer [go <!! <!] :as async]
            [purchase-listinator.misc.either :as either]
            [purchase-listinator.models.internal.shopping-initiation :as models.internal.shopping-initiation]
            [purchase-listinator.dbs.datomic.shopping :as datomic.shopping]
            [purchase-listinator.logic.shopping :as logic.shopping]
            [purchase-listinator.logic.shopping-location :as logic.shopping-location]
            [purchase-listinator.misc.date :as misc.date]
            [purchase-listinator.dbs.mongo.shopping-location :as mongo.shopping-location]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.logic.errors :as logic.errors]
            [purchase-listinator.models.internal.shopping-initiation-data-request :as models.internal.shopping-initiation-data-request]
            [purchase-listinator.dbs.datomic.purchase-list :as dbs.datomic.purchase-list]
            [purchase-listinator.logic.shopping-cart-event :as logic.shopping-cart-event]
            [purchase-listinator.dbs.redis.shopping-cart :as redis.shopping-cart]))

(s/defn init-shopping
  [shopping-initiation :- models.internal.shopping-initiation/ShoppingInitiation
   {:keys [datomic mongo redis]}]
  (either/try-right
    (let [now (misc.date/numb-now)
          {:keys [id] :as shopping} (logic.shopping/initiation->shopping shopping-initiation now)]
      (->> [(go (redis.shopping-cart/init-cart id redis))
            (go (-> (logic.shopping-location/initiation->shopping-location shopping-initiation (misc.general/squuid))
                    (mongo.shopping-location/upsert mongo)))
            (go (datomic.shopping/upsert shopping datomic))]
           (async/map vector)
           <!!
           last))))

(s/defn get-initial-data
  [{:keys [latitude longitude list-id]} :- models.internal.shopping-initiation-data-request/ShoppingInitiationDataRequest
   {:keys [mongo datomic]}]
  (let [near-places (mongo.shopping-location/find-by-location latitude longitude mongo)
        first-near-shopping (and (seq near-places)
                                 (datomic.shopping/get-by-id (-> near-places first :shopping-id) datomic))]
    (if first-near-shopping
      first-near-shopping
      (left {:status 404 :data "not-found"}))))

(s/defn get-in-progress-list
  [shopping-id :- s/Uuid
   {:keys [datomic redis]}]
  (let [cart (redis.shopping-cart/find shopping-id redis)
        {:keys [list-id]} (datomic.shopping/get-by-id shopping-id datomic)
        purchase-list (dbs.datomic.purchase-list/get-management-data list-id datomic)
        shopping (logic.shopping/purchase-list->shopping-list purchase-list)]
    (logic.shopping-cart-event/apply-cart cart shopping)))

(s/defn find-existent
  [list-id :- s/Uuid
   {:keys [datomic]}]
  (if-let [existent (datomic.shopping/get-in-progress-by-list-id list-id datomic)]
    existent
    (left (logic.errors/build 404 nil))))
