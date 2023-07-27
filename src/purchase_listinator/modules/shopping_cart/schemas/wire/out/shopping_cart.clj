(ns purchase-listinator.modules.shopping-cart.schemas.wire.out.shopping-cart
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]))

(def shopping-cart-closed-event-skeleton
  {:purchase-list-id s/Uuid
   :shopping-id s/Uuid
   :cart-events [internal.cart-events/CartEvent]})

(s/defschema ShoppingCartClosedEvent
  shopping-cart-closed-event-skeleton)