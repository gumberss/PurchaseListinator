(ns purchase-listinator.logic.shopping-item
  (:require [purchase-listinator.models.internal.shopping-list :as models.internal.shopping-list]
            [schema.core :as s]))

(s/defn update-item-price :- models.internal.shopping-list/ShoppingItem
  [item :- models.internal.shopping-list/ShoppingItem
   price :- s/Num]
  (assoc item :price price))

(s/defn update-item :- models.internal.shopping-list/ShoppingItem
  [{:keys [quantity-in-cart] :as item} :- models.internal.shopping-list/ShoppingItem
   price :- s/Num
   quantity-changed :- s/Int]
  (-> (update-item-price item price)
      (assoc :quantity-in-cart (+ (or quantity-in-cart 0) (or quantity-changed 0)))))

