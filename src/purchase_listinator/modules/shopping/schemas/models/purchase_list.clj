(ns purchase-listinator.modules.shopping.schemas.models.purchase-list
  (:require [schema.core :as s]))

(def item-skeleton
  {:id             s/Uuid
   :name           s/Str
   :quantity       s/Int
   :order-position s/Int
   :category-id    s/Uuid
   :user-id        s/Uuid})
(s/defschema Item item-skeleton)

(def category-skeleton
  {:name             s/Str
   :id               s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid
   :user-id          s/Uuid
   :items            [Item]})
(s/defschema Category category-skeleton)

(def purchase-list-skeleton
  {:id         s/Uuid
   :categories [Category]})
(s/defschema PurchaseList purchase-list-skeleton)