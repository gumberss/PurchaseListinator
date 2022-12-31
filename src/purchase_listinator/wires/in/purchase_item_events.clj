(ns purchase-listinator.wires.in.purchase-item-events
  (:require [schema.core :as s]))

(def purchase-item-deleted-event-skeleton
  {:item-id     s/Uuid
   :category-id s/Uuid
   :moment      s/Num})
(s/defschema PurchaseItemDeletedEvent purchase-item-deleted-event-skeleton)

(def purchase-item-created-event-skeleton
  {:moment         s/Num
   :item-id        s/Uuid
   :name           s/Str
   :quantity       s/Int
   :order-position s/Int
   :category-id    s/Uuid})
(s/defschema PurchaseItemCreatedEvent purchase-item-created-event-skeleton)

