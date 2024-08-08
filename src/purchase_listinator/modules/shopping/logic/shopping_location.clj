(ns purchase-listinator.modules.shopping.logic.shopping-location
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping.schemas.models.shopping-location :as models.internal.shopping-location]
            [purchase-listinator.modules.shopping.schemas.models.shopping-initiation :as models.internal.shopping-initiation]))

(s/defn initiation->shopping-location :- models.internal.shopping-location/ShoppingLocation
  [{:keys [id] :as shopping-initiation} :- models.internal.shopping-initiation/ShoppingInitiation
   shopping-location-id :- s/Uuid]
  (-> (assoc shopping-initiation :shopping-id id
                                 :id shopping-location-id)
      (select-keys (keys models.internal.shopping-location/ShoppingLocation))))
