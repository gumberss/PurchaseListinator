(ns purchase-listinator.logic.purchase-category
  (:require [purchase-listinator.models.internal.purchase-category :as internal.purchase-category]
            [schema.core :as s]))

(s/defn change-order-position :- internal.purchase-category/PurchaseCategory
  [order-position :- s/Int
   category :- internal.purchase-category/PurchaseCategory]
  (assoc category :order-position (or order-position 0)))

(s/defn increment-order :- internal.purchase-category/PurchaseCategory
  [{:keys [order-position] :as category} :- internal.purchase-category/PurchaseCategory]
  (change-order-position (inc order-position) category))

(s/defn decrement-order :- internal.purchase-category/PurchaseCategory
  [{:keys [order-position] :as category} :- internal.purchase-category/PurchaseCategory]
  (change-order-position (dec order-position) category))

(s/defn reorder :- [internal.purchase-category/PurchaseCategory]
  [old-position :- s/Num
   new-position :- s/Num
   categories :- [internal.purchase-category/PurchaseCategory]]
  (let [increase-position? (< old-position new-position)
        reorder-func #(cond
                        (= old-position (:order-position %)) (change-order-position new-position %)
                        increase-position? (decrement-order %)
                        :else (increment-order %))]
    (map reorder-func categories)))
