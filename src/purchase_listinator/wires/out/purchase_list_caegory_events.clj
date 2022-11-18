(ns purchase-listinator.wires.out.purchase-list-caegory-events
  (:require [schema.core :as s]))

(def purchase-category-deleted-event-skeleton
  {:category-id      s/Uuid
   :purchase-list-id s/Uuid
   :moment           s/Num})

(s/defschema CategoryDeletedEvent purchase-category-deleted-event-skeleton)
