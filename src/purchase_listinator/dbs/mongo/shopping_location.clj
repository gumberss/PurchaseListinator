(ns purchase-listinator.dbs.mongo.shopping-location
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-location :as models.internal.shopping-location]
            [monger.collection :as mc]))

(def collection :shopping-location-test)

(s/defn upsert :- models.internal.shopping-location/ShoppingLocation
  [{:keys [shopping-id] :as shopping-location} :- models.internal.shopping-location/ShoppingLocation
   {:keys [db]}]
  (mc/update db collection {:shopping-id shopping-id} shopping-location {:upsert true})
  shopping-location)
