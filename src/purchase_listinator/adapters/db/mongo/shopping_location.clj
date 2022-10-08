(ns purchase-listinator.adapters.db.mongo.shopping-location
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-location :as models.internal.shopping-location]))

(s/defn internal->db
  [{:keys [latitude longitude] :as internal} :- models.internal.shopping-location/ShoppingLocation]
  (-> internal
      (assoc :latlong {:type "Point" :coordinates [latitude longitude]})
      (dissoc :latitude :longitude)))

(s/defn db->internal :- models.internal.shopping-location/ShoppingLocation
  [{:keys [latlong] :as db}]
  (-> (assoc db :latitude (first latlong) :longitude (second latlong))
      (dissoc :latlong)))
