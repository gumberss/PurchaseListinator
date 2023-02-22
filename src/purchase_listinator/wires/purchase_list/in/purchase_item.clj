(ns purchase-listinator.wires.purchase-list.in.purchase-item
  (:require [schema.core :as s]))

(def purchase-item-skeleton
  {:name             s/Str
   :id               s/Str
   :quantity         s/Int
   :order-position   s/Int
   :user-id          s/Uuid
   :category-id      s/Str})

(s/defschema PurchaseItem purchase-item-skeleton)

(s/defschema PurchaseItems [PurchaseItem])
