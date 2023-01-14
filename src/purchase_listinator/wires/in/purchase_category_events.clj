(ns purchase-listinator.wires.in.purchase-category-events
  (:require [schema.core :as s]))

(def purchase-category-deleted-event-skeleton
  {:event-id               s/Uuid
   :category-id      s/Uuid
   :purchase-list-id s/Uuid
   :moment           s/Num})
(s/defschema PurchaseCategoryDeletedEvent purchase-category-deleted-event-skeleton)

(def purchase-category-created-event-skeleton
  {:event-id               s/Uuid
   :name             s/Str
   :category-id      s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid
   :moment           s/Num})
(s/defschema PurchaseCategoryCreatedEvent purchase-category-created-event-skeleton)
