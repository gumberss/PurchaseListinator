(ns purchase-listinator.logic.shopping-item
  (:require [purchase-listinator.models.internal.shopping-item :as models.internal.shopping-item]
            [schema.core :as s]))

(s/defn fill-quantity-in-cart-empty :- models.internal.shopping-item/ShoppingItem
  [{:keys [quantity-in-cart] :as item} :- models.internal.shopping-item/ShoppingItem]
  (assoc item :quantity-in-cart (or quantity-in-cart 0)))
