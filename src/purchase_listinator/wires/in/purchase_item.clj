(ns purchase-listinator.wires.in.purchase-item
  (:require [schema.core :as s]))

(def purchase-item-skeleton
  {:name             s/Str
   :id               s/Str
   :quantity         s/Int
   :order-position   s/Int
   :category-id      s/Str})

(s/defschema PurchaseItem purchase-item-skeleton)

(s/defschema PurchaseItems [PurchaseItem])
