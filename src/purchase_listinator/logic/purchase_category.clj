(ns purchase-listinator.logic.purchase-category
  (:require [purchase-listinator.models.internal.purchase-category :as internal.purchase-category]
            [schema.core :as s]))

(s/defn change-order-position :- internal.purchase-category/PurchaseCategory
        [order-position :- s/Int
         category :- internal.purchase-category/PurchaseCategory]
        (assoc category :order-position (or order-position 0)))