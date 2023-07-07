(ns purchase-listinator.wires.purchase-list.in.purchase-category-events
  (:require
    [purchase-listinator.misc.schema :as misc.schema]
    [schema.core :as s]))

(def purchase-category-deleted-event-skeleton
  {:event-id         s/Uuid
   :category-id      s/Uuid
   :purchase-list-id s/Uuid
   :user-id          s/Uuid
   :moment           s/Num})
(misc.schema/loose-schema PurchaseCategoryDeletedEvent purchase-category-deleted-event-skeleton)

(def purchase-category-created-event-skeleton
  {:event-id         s/Uuid
   :user-id          s/Uuid
   :name             s/Str
   :category-id      s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid
   :moment           s/Num})
(misc.schema/loose-schema PurchaseCategoryCreatedEvent purchase-category-created-event-skeleton)
