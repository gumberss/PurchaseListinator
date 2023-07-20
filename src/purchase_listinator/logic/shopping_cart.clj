(ns purchase-listinator.logic.shopping-cart
  (:require
    [clojure.data :as data]
    [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
    [purchase-listinator.models.internal.cart :as models.internal.cart]
    [schema.core :as s]))

(s/defn init :- models.internal.shopping-cart/Cart
  [shopping-id :- s/Uuid]
  {:shopping-id shopping-id
   :events      []})

(s/defn ->cart :- models.internal.shopping-cart/Cart
  [shopping-id :- s/Uuid
   {:keys [shopping-cart-events]} :- models.internal.cart/Cart]
  {:shopping-id shopping-id
   :events      shopping-cart-events})



(defn compare-carts
  [map1 map2]
  (data/diff map1 map2))
