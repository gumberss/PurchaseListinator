(ns purchase-listinator.modules.shopping-cart.schemas.wire.out.cart
  (:require [purchase-listinator.misc.schema :as misc.schema]
            [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
            [purchase-listinator.modules.shopping-cart.schemas.internal.purchase-list :as internal.purchase-list]))

(misc.schema/loose-schema Cart
  {:purchase-list        internal.purchase-list/PurchaseList
   :shopping-cart-events [internal.cart-events/CartEvent]})
