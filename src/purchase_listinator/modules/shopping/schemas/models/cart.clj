(ns purchase-listinator.modules.shopping.schemas.models.cart
  (:require
    [purchase-listinator.modules.shopping.schemas.models.cart-events :as internal.cart-events]
    [purchase-listinator.modules.shopping.schemas.models.purchase-list :as internal.shopping.purchase-list]
    [schema.core :as s]))

(s/defschema Cart
  {:purchase-list        internal.shopping.purchase-list/PurchaseList
   :shopping-cart-events [internal.cart-events/CartEvent]})
