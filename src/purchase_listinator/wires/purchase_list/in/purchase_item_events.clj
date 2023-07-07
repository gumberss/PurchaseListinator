(ns purchase-listinator.wires.purchase-list.in.purchase-item-events
  (:require
    [purchase-listinator.misc.schema :as misc.schema]
    [schema.core :as s]))

(def purchase-item-deleted-event-skeleton
  {:event-id    s/Uuid
   :item-id     s/Uuid
   :category-id s/Uuid
   :moment      s/Num
   :user-id     s/Uuid})
(misc.schema/loose-schema PurchaseItemDeletedEvent purchase-item-deleted-event-skeleton)

(def purchase-item-created-event-skeleton
  {:event-id       s/Uuid
   :moment         s/Num
   :item-id        s/Uuid
   :name           s/Str
   :quantity       s/Int
   :order-position s/Int
   :category-id    s/Uuid
   :user-id        s/Uuid})
(misc.schema/loose-schema PurchaseItemCreatedEvent purchase-item-created-event-skeleton)

(def purchase-item-updated-event-skeleton
  (select-keys purchase-item-created-event-skeleton
               [:item-id :quantity :name :category-id :order-position :moment :event-id :user-id]))
(misc.schema/loose-schema PurchaseItemChangedEvent purchase-item-updated-event-skeleton)
