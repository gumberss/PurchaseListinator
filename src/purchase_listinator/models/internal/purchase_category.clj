(ns purchase-listinator.models.internal.purchase-category
  (:require [schema.core :as s]))

(def purchase-category-skeleton
  {:name             s/Str
   :id               s/Uuid
   :order-position   s/Int
   :color            s/Str
   :purchase-list-id s/Uuid})
(s/defschema PurchaseCategory purchase-category-skeleton)
(s/defschema PurchaseCategories [PurchaseCategory])
