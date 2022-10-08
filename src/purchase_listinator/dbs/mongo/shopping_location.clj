(ns purchase-listinator.dbs.mongo.shopping-location
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-location :as models.internal.shopping-location]
            [monger.collection :as mc]))

(def collection :shopping-location-test)

(s/defn insert :- models.internal.shopping-location/ShoppingLocation
  [shopping-location :- models.internal.shopping-location/ShoppingLocation
   {:keys [db]}]
  (mc/insert db collection shopping-location)
  shopping-location)
