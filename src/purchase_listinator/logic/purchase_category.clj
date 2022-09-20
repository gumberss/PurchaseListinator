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

(s/defn ^:private reposition-one
  [old-position :- s/Num
   new-position :- s/Num
   {:keys [order-position] :as category} :- internal.purchase-category/PurchaseCategory]
  (let [decrease-position? (< old-position new-position)
        the-category-reordered (= old-position order-position)
        reposition-way (if decrease-position? decrement-order increment-order)]
    (if the-category-reordered
      (change-order-position new-position category)
      (reposition-way category))))

(s/defn reposition :- [internal.purchase-category/PurchaseCategory]
  [old-position :- s/Num
   new-position :- s/Num
   categories :- [internal.purchase-category/PurchaseCategory]]
  (let [reposition-func* (partial reposition-one old-position new-position)]
    (map reposition-func* categories)))

(s/defn sort-items-by-position :- internal.purchase-category/PurchaseCategory
  [{:keys [purchase-items] :as category} :- internal.purchase-category/PurchaseCategory]
  (->> (sort-by :order-position purchase-items)
       (assoc category :purchase-items)))

(s/defn sort-by-position :- [internal.purchase-category/PurchaseCategory]
  [categories :- [internal.purchase-category/PurchaseCategory]]
  (sort-by :order-position categories))
