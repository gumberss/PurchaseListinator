(ns purchase-listinator.wires.purchase-list.in.purchase-category
  (:require [schema.core :as s]))

(def purchase-category-skeleton
  {:name             s/Str
   :id               s/Str
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Str})
(s/defschema PurchaseCategory purchase-category-skeleton)
(s/defschema PurchaseCategories [PurchaseCategory])