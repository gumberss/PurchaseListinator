(ns purchase-listinator.wires.purchase-list.out.purchase-list-category-events
  (:require [schema.core :as s]))

(def base-purchase-category-event-skeleton
  {:event-id         s/Uuid
   :user-id          s/Uuid
   :category-id      s/Uuid
   :purchase-list-id s/Uuid
   :moment           s/Num})

(def purchase-category-deleted-event-skeleton
  base-purchase-category-event-skeleton)
(s/defschema CategoryDeletedEvent purchase-category-deleted-event-skeleton)

(def purchase-category-created-event-skeleton
  (merge base-purchase-category-event-skeleton
         {:name           s/Str
          :order-position s/Int
          :color          s/Int}))
(s/defschema CategoryCreatedEvent purchase-category-created-event-skeleton)
