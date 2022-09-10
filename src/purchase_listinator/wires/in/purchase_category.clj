(ns purchase-listinator.wires.in.purchase-category
  (:require [schema.core :as s]))

(def purchase-category-skeleton
  {:name             s/Str
   :id               s/Str
   :quantity         s/Int
   :order-position   s/Int
   :color            s/Str
   :purchase-list-id s/Str})
(s/defschema PurchaseCategory purchase-category-skeleton)
(s/defschema PurchaseCategories [PurchaseCategory])
