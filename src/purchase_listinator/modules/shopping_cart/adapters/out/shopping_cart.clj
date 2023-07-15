(ns purchase-listinator.modules.shopping-cart.adapters.out.shopping-cart
  (:require
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [purchase-listinator.modules.shopping-cart.schemas.internal.shopping :as internal.shopping]
    [schema.core :as s]))

(s/defn internal->shopping-cart-closed-event
  [{:keys [list-id id]} :- internal.shopping/Shopping
   events :- [internal.cart-events/CartEvent]]
  {:purchase-list-id list-id
   :shopping-id      id
   :cart-events      events})