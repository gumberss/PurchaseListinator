(ns purchase-listinator.logic.shopping-cart
  (:require [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
            [schema.core :as s]))

(s/defn init :- models.internal.shopping-cart/Cart
  [shopping-id :- s/Uuid]
  {:shopping-id shopping-id
   :events      []})
