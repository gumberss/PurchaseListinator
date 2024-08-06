(ns purchase-listinator.modules.shopping.adapters.in.cart
  (:require
    [purchase-listinator.modules.shopping.schemas.models.cart :as models.internal.cart]
    [purchase-listinator.modules.shopping.schemas.wires.in.cart :as wires.in.cart]
    [schema.core :as s]))

(s/defn wire->internal :- models.internal.cart/Cart
  [wire :- wires.in.cart/Cart]
  wire)