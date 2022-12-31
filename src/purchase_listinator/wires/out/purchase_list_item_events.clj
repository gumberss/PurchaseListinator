(ns purchase-listinator.wires.out.purchase-list-item-events
  (:require [schema.core :as s]))

(def purchase-item-deleted-event-skeleton
  {:category-id      s/Uuid
   :purchase-list-id s/Uuid
   :moment           s/Num})

(s/defschema ItemDeletedEvent purchase-item-deleted-event-skeleton)

(def purchase-item-created-event-skeleton
  {:name             s/Str
   :category-id      s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid
   :moment           s/Num})

(s/defschema ItemCreatedEvent purchase-item-created-event-skeleton)

