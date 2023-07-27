(ns purchase-listinator.modules.shopping-cart.adapters.out.shopping-cart
  (:require
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [purchase-listinator.modules.shopping-cart.schemas.internal.shopping :as internal.shopping]
    [purchase-listinator.modules.shopping-cart.schemas.wire.out.shopping-cart :as wire.out.shopping-cart]
    [schema.core :as s]))

(s/defn internal->shopping-cart-closed-event :- wire.out.shopping-cart/ShoppingCartClosedEvent
  [{:keys [list-id id]} :- internal.shopping/Shopping
   events :- [internal.cart-events/CartEvent]]
  {:purchase-list-id list-id
   :shopping-id      id
   :cart-events      events})