(ns purchase-listinator.models.internal.shopping
  (:require [schema.core :as s]))

(def status (s/enum :in-progress :done :canceled))
(s/defschema Status status)

(def shopping-item-skeleton
  {:id               s/Uuid
   :name             s/Str
   :quantity         s/Int
   :price            s/Num
   :quantity-in-cart s/Int
   :order-position   s/Int
   :category-id      s/Uuid})
(s/defschema ShoppingItem shopping-item-skeleton)

(def shopping-category-skeleton
  {:name             s/Str
   :id               s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid
   :items            [ShoppingItem]})
(s/defschema ShoppingCategory shopping-category-skeleton)

(def shopping-skeleton
  {:id                        s/Uuid
   :place                     s/Str
   :type                      s/Str
   :title                     s/Str
   :date                      s/Num
   :list-id                   s/Uuid
   :status                    Status
   (s/optional-key :duration) s/Num
   (s/optional-key :categories) s/Num})
(s/defschema Shopping shopping-skeleton)

