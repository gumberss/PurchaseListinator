(ns purchase-listinator.modules.events.diplomat.consumers.shopping-events
  (:require [schema.core :as s]
            [purchase-listinator.modules.events.adapters.in.shopping-cart-events :as adapters.in.cart-events]
            [purchase-listinator.modules.events.flows.receive-shopping-events :as events.flows.shopping-events]
            [purchase-listinator.modules.events.schemas.wires.in.cart-events :as wires.in.shopping-cart-events]))

(s/defn shopping-cart-events-receive
  [_channel
   _metadata
   components
   event :- wires.in.shopping-cart-events/ShoppingCartClosedEvent]
  (-> (adapters.in.cart-events/wire->internal event)
      (events.flows.shopping-events/receive-cart-events components)))

(def subscribers
  [{:exchange :shopping-cart/shopping-cart.closed
    :queue    :purchase-listinator.events/shopping-cart-events.receive
    :schema   wires.in.shopping-cart-events/ShoppingCartClosedEvent
    :handler  shopping-cart-events-receive}])
