(ns purchase-listinator.wires.out.purchase-list-item-events
  (:require [schema.core :as s]))

(def purchase-item-deleted-event-skeleton
  {:category-id      s/Uuid
   :purchase-list-id s/Uuid
   :moment           s/Num})
(s/defschema ItemDeletedEvent purchase-item-deleted-event-skeleton)

(def purchase-item-created-event-skeleton
  {:item-id        s/Uuid
   :name           s/Str
   :category-id    s/Uuid
   :order-position s/Int
   :quantity       s/Int
   :moment         s/Num})
(s/defschema ItemCreatedEvent purchase-item-created-event-skeleton)

(def purchase-item-changed-event-skeleton
  (select-keys purchase-item-created-event-skeleton
               [:item-id :quantity :name :category-id :order-position :moment]))
(s/defschema ItemChangedEvent purchase-item-changed-event-skeleton)



