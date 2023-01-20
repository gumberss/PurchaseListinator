(ns purchase-listinator.logic.purchase-item
  (:require [purchase-listinator.models.internal.purchase-list.purchase-item :as internal.purchase-item]
            [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list.purchase-item :as models.internal.purchase-item]
            [purchase-listinator.models.internal.purchase-list.shopping :as models.internal.purchase-list.shopping]))

(s/defn change-order-position :- internal.purchase-item/PurchaseItem
  [order-position :- (s/maybe s/Int)
   item :- internal.purchase-item/PurchaseItem]
  (assoc item :order-position (or order-position 0)))

(s/defn sort-by-position :- internal.purchase-item/PurchaseItems
  [items :- internal.purchase-item/PurchaseItems]
  (sort-by :order-position items))

(s/defn update-quantity-by-shopping-item :- models.internal.purchase-item/PurchaseItem
  [{:keys [quantity] :as purchase-item} :- models.internal.purchase-item/PurchaseItem
   {:keys [quantity-in-cart]} :- models.internal.purchase-list.shopping/ShoppingItem]
  (assoc purchase-item :quantity (- quantity quantity-in-cart)))

(s/defn find-by-shopping-item :- models.internal.purchase-item/PurchaseItem
  [purchase-items :- [models.internal.purchase-item/PurchaseItem]
   {:keys [id]} :- models.internal.purchase-list.shopping/ShoppingItem]
  (-> (filter #(= (:id %) id) purchase-items)
      first))

(s/defn find-item-and-update-item-quantity :- models.internal.purchase-item/PurchaseItem
  [purchase-items :- [models.internal.purchase-item/PurchaseItem]
   shopping-item :- models.internal.purchase-list.shopping/ShoppingItem]
  (-> (find-by-shopping-item purchase-items shopping-item)
      (update-quantity-by-shopping-item shopping-item)))

(s/defn build-items-pair :- [models.internal.purchase-item/PurchaseItem]
  [old-items :- [models.internal.purchase-item/PurchaseItem]
   new-items :- [models.internal.purchase-item/PurchaseItem]]
  (->> (concat old-items new-items)
       (group-by :id)
       vals))
