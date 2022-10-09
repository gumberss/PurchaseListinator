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
            [purchase-listinator.dbs.redis.shopping :as redis.shopping]))

(s/defn init-shopping
  [shopping-initiation :- models.internal.shopping-initiation/ShoppingInitiation
   {:keys [datomic mongo redis]}]
  (either/try-right
    (let [now (misc.date/numb-now)
          {:keys [id] :as shopping} (logic.shopping/initiation->shopping shopping-initiation now)]
      (->> [(go (redis.shopping/init-cart id redis))
            (go (-> (logic.shopping-location/initiation->shopping-location shopping-initiation (misc.general/squuid))
                    (mongo.shopping-location/upsert mongo)))
            (go (datomic.shopping/upsert shopping datomic))]
           (async/map vector)
           <!!
           last))))

(s/defn find-existent
  [list-id :- s/Uuid
   {:keys [datomic]}]
  (if-let [existent (datomic.shopping/get-in-progress-by-list-id list-id datomic)]
    existent
    (left (logic.errors/build 404 nil))))
