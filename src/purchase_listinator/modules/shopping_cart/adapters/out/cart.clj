(ns purchase-listinator.modules.shopping-cart.adapters.out.cart
  (:require [purchase-listinator.modules.shopping-cart.schemas.internal.cart :as internal.cart]
            [purchase-listinator.modules.shopping-cart.schemas.wire.out.cart :as wire.out.cart]
            [schema.core :as s]))

(s/defn internal->wire :- wire.out.cart/Cart
  [{:keys [list events]} :- internal.cart/Cart]
  {:purchase-list        list
   :shopping-cart-events events})