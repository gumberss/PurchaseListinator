(ns purchase-listinator.models.internal.purchase-list.shopping
  (:require [schema.core :as s]))

(def shopping-item-skeleton
  {:id               s/Uuid
   :quantity-in-cart s/Int})
(s/defschema ShoppingItem shopping-item-skeleton)

(def shopping-category-skeleton
  {:name           s/Str
   :id             s/Uuid
   :items          [ShoppingItem]})
(s/defschema ShoppingCategory shopping-category-skeleton)

(def status (s/enum :in-progress :done :canceled))
(s/defschema Status status)

(def shopping-skeleton
  {:id                          s/Uuid
   :list-id                     s/Uuid
   :status                      Status
   (s/optional-key :categories) [ShoppingCategory]})
(s/defschema Shopping shopping-skeleton)

