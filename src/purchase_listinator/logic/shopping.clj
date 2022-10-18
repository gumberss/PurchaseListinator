(ns purchase-listinator.logic.shopping
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]
            [purchase-listinator.models.internal.purchase-list-management-data :as purchase-list-management-data]
            [purchase-listinator.models.internal.shopping-management-data :as shopping-management-data]
            [purchase-listinator.models.internal.shopping-initiation :as models.internal.shopping-initiation]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]))

(s/defn initiation->shopping :- models.internal.shopping/Shopping
  [shopping :- models.internal.shopping-initiation/ShoppingInitiation
   now :- s/Num]
  (-> (assoc shopping :status :in-progress
                      :date now)
      (select-keys (keys models.internal.shopping/Shopping))))


(s/defn purchase-list->shopping :- shopping-management-data/ManagementData
  [purchase-list  :- purchase-list-management-data/ManagementData]
  purchase-list)

