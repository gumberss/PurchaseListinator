(ns purchase-listinator.wires.in.purchase-category-events
  (:require [schema.core :as s]))

(def purchase-category-deleted-event-skeleton
  {:category-id      s/Uuid
   :purchase-list-id s/Uuid
   :moment           s/Num})

(s/defschema PurchaseCategoryDeletedEvent purchase-category-deleted-event-skeleton)
