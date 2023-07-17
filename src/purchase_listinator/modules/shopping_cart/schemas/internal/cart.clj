(ns purchase-listinator.modules.shopping-cart.schemas.internal.cart
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
            [purchase-listinator.modules.shopping-cart.schemas.internal.purchase-list :as internal.purchase-list]))

(s/def cart-skeleton
  {:list   internal.purchase-list/PurchaseList
   :events [internal.cart-events/CartEvent]})

(s/defschema Cart
  cart-skeleton)
