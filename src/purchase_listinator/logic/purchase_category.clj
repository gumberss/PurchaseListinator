(ns purchase-listinator.logic.purchase-category
  (:require [purchase-listinator.models.internal.purchase-category :as internal.purchase-category]
            [schema.core :as s]))

(s/defn sort-items-by-position :- internal.purchase-category/PurchaseCategory
  [{:keys [purchase-items] :as category} :- internal.purchase-category/PurchaseCategory]
  (->> (sort-by :order-position purchase-items)
       (assoc category :purchase-items)))

(s/defn sort-by-position :- [internal.purchase-category/PurchaseCategory]
  [categories :- [internal.purchase-category/PurchaseCategory]]
  (sort-by :order-position categories))
