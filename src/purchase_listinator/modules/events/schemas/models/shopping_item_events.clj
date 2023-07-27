(ns purchase-listinator.modules.events.schemas.models.shopping-item-events
  (:require
    [purchase-listinator.modules.events.schemas.models.cart-events :as models.cart-events]
    [schema.core :as s]))

(s/defschema ShoppingCartItemEventCollection
  (apply conj {:item-id s/Uuid} models.cart-events/ShoppingCartEventCollection))