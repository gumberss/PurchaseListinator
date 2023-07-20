(ns purchase-listinator.modules.shopping-cart.schemas.wire.out.cart
  (:require [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
            [purchase-listinator.modules.shopping-cart.schemas.internal.purchase-list :as internal.purchase-list]
            [schema.core :as s]))

(s/defschema Cart
  {:purchase-list        internal.purchase-list/PurchaseList
   :shopping-cart-events [internal.cart-events/CartEvent]})
