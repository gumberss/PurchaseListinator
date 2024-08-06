(ns purchase-listinator.modules.shopping.schemas.models.shopping-category
  (:require [purchase-listinator.modules.shopping.schemas.models.shopping-item :as models.internal.shopping-item]
            [schema.core :as s]))

(def shopping-category-skeleton
  {:name           s/Str
   :id             s/Uuid
   :order-position s/Int
   :color          s/Int
   :shopping-id    s/Uuid
   :items          [models.internal.shopping-item/ShoppingItem]})
(s/defschema ShoppingCategory shopping-category-skeleton)
