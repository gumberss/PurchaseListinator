(ns purchase-listinator.wires.purchase-list.out.purchase-list-item-events
  (:require [schema.core :as s]))

(def purchase-item-deleted-event-skeleton
  {:event-id         s/Uuid
   :user-id          s/Uuid
   :category-id      s/Uuid
   :purchase-list-id s/Uuid
   :moment           s/Num})
(s/defschema ItemDeletedEvent purchase-item-deleted-event-skeleton)

(def purchase-item-created-event-skeleton
  {:event-id         s/Uuid
   :item-id          s/Uuid
   :user-id          s/Uuid
   :name             s/Str
   :category-id      s/Uuid
   :order-position   s/Int
   :quantity         s/Int
   :moment           s/Num
   :purchase-list-id s/Uuid})
(s/defschema ItemCreatedEvent purchase-item-created-event-skeleton)

(def purchase-item-changed-event-skeleton
  (select-keys purchase-item-created-event-skeleton
               [:item-id :quantity :name :category-id :order-position :moment :event-id :user-id]))
(s/defschema ItemChangedEvent purchase-item-changed-event-skeleton)
