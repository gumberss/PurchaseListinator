(ns purchase-listinator.logic.purchase-item
  (:require [purchase-listinator.models.internal.purchase-item :as internal.purchase-item]
            [schema.core :as s]))

(s/defn change-order-position :- internal.purchase-item/PurchaseItem
  [order-position :- s/Int
   category :- internal.purchase-item/PurchaseItem]
  (assoc category :order-position (or order-position 0)))
