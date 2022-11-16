(ns purchase-listinator.endpoints.queue.shopping-purchase-list-event-received
  (:require [purchase-listinator.wires.in.purchase-category-events :as wires.in.purchase-category-events]
            [schema.core :as s]))

(defn purchase-list-event-received
  [_channel
   _metadata
   components
   payload]
  (println payload)
  (println "components")
  (println (keys components)))

(s/defn purchase-list-category-deleted-event-received
  [_channel
   _metadata
   components
   {:keys [category-id purchase-list-id]} :- wires.in.purchase-category-events/PurchaseCategoryDeletedEvent]
  (println category-id)
  (println purchase-list-id))

(def subscribers
  [{:exchange :purchase-listinator/purchase-list.updated
    :queue   :purchase-listinator/shopping-list.update
    :handler purchase-list-event-received}
   {:exchange :purchase-listinator/purchase-list.category.deleted
    :queue   :purchase-listinator/shopping-list.category.delete
    :schema wires.in.purchase-category-events/PurchaseCategoryDeletedEvent
    :handler purchase-list-category-deleted-event-received}])
