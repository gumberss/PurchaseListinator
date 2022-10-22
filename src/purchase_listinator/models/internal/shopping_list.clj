(ns purchase-listinator.models.internal.shopping-list
  (:require [schema.core :as s]))

(def shopping-item-skeleton
  {:id             s/Uuid
   :name           s/Str
   :quantity       s/Int
   :order-position s/Int
   :category-id    s/Uuid})
(s/defschema ShoppingItem shopping-item-skeleton)

(def shopping-category-skeleton
  {:name             s/Str
   :id               s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid
   :items            [ShoppingItem]})
(s/defschema ShoppingListCategory shopping-category-skeleton)

(def shopping-list-skeleton
  {:purchase-list-id s/Uuid
   :shopping-id      s/Uuid
   :categories       [ShoppingListCategory]})
(s/defschema ShoppingList shopping-list-skeleton)

