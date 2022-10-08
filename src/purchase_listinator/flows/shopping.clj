(ns purchase-listinator.flows.shopping
  (:require [schema.core :as s]
            [cats.monad.either :refer [left right]]
            [purchase-listinator.misc.either :as either]
            [purchase-listinator.models.internal.shopping-initiation :as models.internal.shopping-initiation]
            [purchase-listinator.dbs.datomic.shopping :as datomic.shopping]
            [purchase-listinator.logic.shopping :as logic.shopping]
            [purchase-listinator.logic.shopping-location :as logic.shopping-location]
            [purchase-listinator.misc.date :as misc.date]
            [purchase-listinator.dbs.mongo.shopping-location :as mongo.shopping-location]
            [purchase-listinator.misc.general :as misc.general]))

(s/defn init-shopping
  [shopping-initiation :- models.internal.shopping-initiation/ShoppingInitiation
   datomic
   mongo]
  (either/try-right
    (let [now (misc.date/numb-now)]
      (-> (logic.shopping-location/initiation->shopping-location shopping-initiation (misc.general/squuid))
          (mongo.shopping-location/insert mongo))
      #_(-> (logic.shopping/initiation->shopping shopping-initiation now)
          (datomic.shopping/upsert datomic))
      )))
