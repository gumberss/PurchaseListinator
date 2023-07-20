(ns purchase-listinator.adapters.in.cart
  (:require
    [purchase-listinator.models.internal.cart :as models.internal.cart]
    [purchase-listinator.wires.in.shopping-cart :as wires.in.shopping-cart]
    [schema.core :as s]))

(s/defn wire->internal :- models.internal.cart/Cart
  [wire :- wires.in.shopping-cart/Cart]
  wire)