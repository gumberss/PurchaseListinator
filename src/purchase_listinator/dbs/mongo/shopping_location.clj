(ns purchase-listinator.dbs.mongo.shopping-location
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-location :as models.internal.shopping-location]
            [purchase-listinator.adapters.db.mongo.shopping-location :as adapters.db.mongo.shopping-location]
            [monger.collection :as mc]))

(def collection :shopping-location-test)

(defn indexes
  [db]
  (mc/ensure-index db collection (array-map :latlong "2dsphere")))

(s/defn upsert :- models.internal.shopping-location/ShoppingLocation
  [{:keys [shopping-id] :as shopping-location} :- models.internal.shopping-location/ShoppingLocation
   {:keys [db]}]
  (let [db-shopping-location (adapters.db.mongo.shopping-location/internal->db shopping-location)]
    (mc/update db collection {:shopping-id shopping-id} db-shopping-location {:upsert true}))
  shopping-location)
