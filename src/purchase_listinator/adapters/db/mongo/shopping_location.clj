(ns purchase-listinator.adapters.db.mongo.shopping-location
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-location :as models.internal.shopping-location]))

(s/defn internal->db
  [{:keys [latitude longitude] :as internal} :- models.internal.shopping-location/ShoppingLocation]
  (-> internal
      (assoc :latlong {:type "Point" :coordinates [longitude latitude]})
      (dissoc :latitude :longitude)))

(s/defn db->internal :- models.internal.shopping-location/ShoppingLocation
  [{{coordinates :coordinates} :latlong :as db}]
  (-> (assoc db :longitude (first coordinates) :latitude (second coordinates))
      (dissoc :latlong :_id)))
