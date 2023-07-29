(ns purchase-listinator.adapters.in.cart
  (:require
    [purchase-listinator.models.internal.cart :as models.internal.cart]
    [purchase-listinator.wires.in.cart :as wires.in.cart]
    [schema.core :as s]))

(s/defn wire->internal :- models.internal.cart/Cart
  [wire :- wires.in.cart/Cart]
  wire)