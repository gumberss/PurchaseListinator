(ns purchase-listinator.modules.shopping-cart.logic.cart
  (:require
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart :as internal.cart]
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [purchase-listinator.modules.shopping-cart.schemas.internal.purchase-list :as internal.purchase-list]
    [schema.core :as s]))

(s/defn ->cart :- internal.cart/Cart
  [list :- internal.purchase-list/PurchaseList
   events :- [internal.cart-events/CartEvent]]
  {:list   list
   :events events})