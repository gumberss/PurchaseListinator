(ns purchase-listinator.models.internal.purchase-list.purchase-item
  (:require [schema.core :as s]))

(def purchase-item-skeleton
  {:id                       s/Uuid
   :name                     s/Str
   (s/optional-key :user-id) s/Uuid
   :quantity                 s/Int
   :order-position           s/Int
   :category-id              s/Uuid})
(s/defschema PurchaseItem purchase-item-skeleton)
(s/defschema PurchaseItems [PurchaseItem])
