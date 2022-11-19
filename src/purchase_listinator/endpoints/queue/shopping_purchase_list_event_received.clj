(ns purchase-listinator.endpoints.queue.shopping-purchase-list-event-received
  (:require [purchase-listinator.wires.in.purchase-category-events :as wires.in.purchase-category-events]
            [purchase-listinator.adapters.in.shopping-purchase-list-events :as adapters.in.shopping-purchase-list-events]
            [schema.core :as s]
            [purchase-listinator.misc.date :as misc.date]
            [purchase-listinator.flows.shopping :as flows.shopping]))

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
   event :- wires.in.purchase-category-events/PurchaseCategoryDeletedEvent]
  (-> (adapters.in.shopping-purchase-list-events/category-deleted-event->internal event)
      (flows.shopping/receive-cart-event-by-list components)))

(s/defn purchase-list-category-created-event-received
  [_channel
   _metadata
   components
   event :- wires.in.purchase-category-events/PurchaseCategoryCreatedEvent]
  (-> (adapters.in.shopping-purchase-list-events/category-created-event->internal event)
      (flows.shopping/receive-cart-event-by-list components)))

(def subscribers
  [{:exchange :purchase-listinator/purchase-list.updated
    :queue    :purchase-listinator/shopping-list.update
    :handler  purchase-list-event-received}
   {:exchange :purchase-listinator/purchase-list.category.deleted
    :queue    :purchase-listinator/shopping-list.category.delete
    :schema   wires.in.purchase-category-events/PurchaseCategoryDeletedEvent
    :handler  purchase-list-category-deleted-event-received}
   {:exchange :purchase-listinator/purchase-list.category.created
    :queue    :purchase-listinator/shopping-list.category.create
    :schema   wires.in.purchase-category-events/PurchaseCategoryCreatedEvent
    :handler  purchase-list-category-created-event-received}])


