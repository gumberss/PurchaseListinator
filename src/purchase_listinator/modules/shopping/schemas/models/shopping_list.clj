(ns purchase-listinator.modules.shopping.schemas.models.shopping-list
  (:require [schema.core :as s]))

(def shopping-item-skeleton
  {:id               s/Uuid
   :name             s/Str
   :quantity         s/Int
   :price            (s/maybe s/Num)
   :quantity-in-cart s/Int
   :order-position   s/Int
   :user-id          s/Uuid
   :category-id      s/Uuid})
(s/defschema ShoppingItem shopping-item-skeleton)

(def shopping-category-skeleton
  {:name             s/Str
   :id               s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid
   :user-id          s/Uuid
   :items            [ShoppingItem]})
(s/defschema ShoppingListCategory shopping-category-skeleton)

(def shopping-list-skeleton
  {:purchase-list-id s/Uuid
   :shopping-id      s/Uuid
   :user-id          s/Uuid
   :categories       [ShoppingListCategory]})
(s/defschema ShoppingList shopping-list-skeleton)

(s/defschema ManagementData (dissoc shopping-list-skeleton :shopping-id))

