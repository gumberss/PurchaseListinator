(ns purchase-listinator.models.internal.cart
  (:require
    [purchase-listinator.models.internal.cart-events :as internal.cart-events]
    [purchase-listinator.models.internal.shopping.purchase-list :as internal.shopping.purchase-list]
    [schema.core :as s]))

(s/defschema Cart
  {:purchase-list        internal.shopping.purchase-list/PurchaseList
   :shopping-cart-events [internal.cart-events/CartEvent]})
