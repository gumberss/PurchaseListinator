(ns purchase-listinator.logic.shopping
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]
            [purchase-listinator.models.internal.shopping-initiation :as models.internal.shopping-initiation]))

(s/defn initiation->shopping :- models.internal.shopping/Shopping
  [shopping :- models.internal.shopping-initiation/ShoppingInitiation
   now :- s/Num]
  (-> (assoc shopping :status :in-progress
                      :date now)
      (select-keys (keys models.internal.shopping/Shopping))))
