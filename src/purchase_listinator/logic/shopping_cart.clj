(ns purchase-listinator.logic.shopping-cart
  (:require
    [purchase-listinator.models.internal.cart :as models.internal.cart]
    [purchase-listinator.models.internal.cart-events :as internal.cart-events]
    [schema.core :as s]))

(s/defn ->shopping-cart :- internal.cart-events/Cart
  [shopping-id :- s/Uuid
   {:keys [shopping-cart-events]} :- models.internal.cart/Cart]
  {:shopping-id shopping-id
   :events      shopping-cart-events})

