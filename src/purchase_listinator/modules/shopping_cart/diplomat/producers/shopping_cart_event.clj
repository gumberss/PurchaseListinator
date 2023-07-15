(ns purchase-listinator.modules.shopping-cart.diplomat.producers.shopping-cart-event
  (:require
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [purchase-listinator.modules.shopping-cart.schemas.internal.shopping :as internal.shopping]
    [purchase-listinator.components.rabbitmq-channel :as components.rabbitmq-channel]
    [purchase-listinator.modules.shopping-cart.adapters.out.shopping-cart :as adapters.out.shopping-cart]
    [schema.core :as s]))

(s/defn shopping-cart-closed
  [shopping :- internal.shopping/Shopping
   events :- [internal.cart-events/CartEvent]
   producer :- components.rabbitmq-channel/IProducer]
   (->> (adapters.out.shopping-cart/internal->shopping-cart-closed-event shopping events)
        (components.rabbitmq-channel/publish producer
                                             :shopping-cart/shopping-cart.closed)))