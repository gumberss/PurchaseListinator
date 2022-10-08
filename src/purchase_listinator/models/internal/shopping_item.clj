(ns purchase-listinator.models.internal.shopping-item
  (:require [schema.core :as s]))

(def shopping-item-skeleton
  {:id               s/Uuid
   :name             s/Str
   :quantity         s/Str
   :unit-price       s/Num
   :shopping-id      s/Uuid
   :purchase-item-id s/Uuid})
(s/defschema ShoppingItem shopping-item-skeleton)

