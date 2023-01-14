(ns purchase-listinator.models.internal.shopping-item
  (:require [schema.core :as s]))

(def shopping-item-skeleton
  {:id               s/Uuid
   :name             s/Str
   :quantity         s/Int
   :price            s/Num
   :quantity-in-cart s/Int
   :order-position   s/Int
   :category-id      s/Uuid})
(s/defschema ShoppingItem shopping-item-skeleton)
