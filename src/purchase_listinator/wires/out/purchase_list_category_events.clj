(ns purchase-listinator.wires.out.purchase-list-category-events
  (:require [schema.core :as s]))

(def purchase-category-deleted-event-skeleton
  {:event-id               s/Uuid
   :category-id      s/Uuid
   :purchase-list-id s/Uuid
   :moment           s/Num})

(s/defschema CategoryDeletedEvent purchase-category-deleted-event-skeleton)

(def purchase-category-created-event-skeleton
  {:event-id               s/Uuid
   :name             s/Str
   :category-id      s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid
   :moment           s/Num})

(s/defschema CategoryCreatedEvent purchase-category-created-event-skeleton)
