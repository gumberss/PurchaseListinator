(ns purchase-listinator.endpoints.queue.purchase-list-shopping-event-received
  (:require [schema.core :as s]
            [purchase-listinator.wires.purchase-list.in.shopping :as wires.purchase-list.in.shopping]
            [purchase-listinator.flows.purchase-item :as flows.purchase-item]))

(s/defn shopping-finished-event-received
  [_channel
   _metadata
   components
   event :- wires.purchase-list.in.shopping/ShoppingFinishedEvent]
  (flows.purchase-item/receive-shopping-finished (:shopping event) components))

(def subscribers
  [{:exchange :purchase-listinator/shopping.finished
    :queue    :purchase-listinator/purchase-list.shopping-finished.receive
    :schema   wires.purchase-list.in.shopping/ShoppingFinishedEvent
    :handler  shopping-finished-event-received}])
