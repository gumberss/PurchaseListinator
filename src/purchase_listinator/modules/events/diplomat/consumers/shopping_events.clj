(ns purchase-listinator.modules.events.diplomat.consumers.shopping-events
  (:require [schema.core :as s]
            [purchase-listinator.modules.events.schemas.wires.in.shopping-finished-event :as wires.in.shopping-finished-event]
            [purchase-listinator.modules.events.adapters.in.shopping-events :as adapters.in.shopping-events]
            [purchase-listinator.modules.events.flows.receive-shopping-events :as events.flows.shopping-events]))

(s/defn shopping-events-received
  [_channel
   _metadata
   components
   event :- wires.in.shopping-finished-event/ShoppingFinishedEvent]
  (-> (adapters.in.shopping-events/wire->internal event)
      (events.flows.shopping-events/receive-events components)))

(s/defn shopping-cart-events-receive
  [_channel
   _metadata
   components
   event]
  (clojure.pprint/pprint event))

(def subscribers
  [{:exchange :purchase-listinator/shopping.finished
    :queue    :purchase-listinator.events/shopping-events-receive
    :schema   wires.in.shopping-finished-event/ShoppingFinishedEvent
    :handler  shopping-events-received}
   {:exchange :shopping-cart/shopping-cart.closed
    :queue    :purchase-listinator.events/shopping-cart-events.receive
    ;:schema   wires.in.shopping-finished-event/ShoppingFinishedEvent
    :handler  shopping-cart-events-receive}])
