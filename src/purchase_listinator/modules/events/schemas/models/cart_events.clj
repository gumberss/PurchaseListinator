(ns purchase-listinator.modules.events.schemas.models.cart-events
  (:require [schema.core :as s]))

(s/defschema ShoppingCartEventProperty
  {s/Keyword s/Any})

(s/defschema ShoppingCartEvent
  {:id          s/Uuid
   :moment      s/Num
   :event-type  s/Keyword
   :user-id     s/Uuid
   :shopping-id s/Uuid
   :item-id     (s/maybe s/Uuid)
   :category-id (s/maybe s/Uuid)
   :properties  ShoppingCartEventProperty})

(s/defschema ShoppingCartEventCollection
  {:events [ShoppingCartEvent]})