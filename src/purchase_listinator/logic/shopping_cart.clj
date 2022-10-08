(ns purchase-listinator.logic.shopping-cart
  (:require [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
            [schema.core :as s]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]))

(s/defn shopping->initial-cart :- models.internal.shopping-cart/Cart
  [shopping :- models.internal.shopping/Shopping]
  (assoc shopping :items []))
