(ns purchase-listinator.modules.events.schemas.models.cart-events
  (:require [schema.core :as s]))

(s/defschema ShoppingEventProperty
  {s/Keyword s/Any})

(s/defschema CartEvent
  {:id          s/Uuid
   :moment      s/Num
   :event-type  s/Keyword
   :user-id     s/Uuid
   :shopping-id s/Uuid
   :item-id     (s/maybe s/Uuid)
   :category-id (s/maybe s/Uuid)
   :properties  ShoppingEventProperty})

(s/defschema CartEventCollection
  {:events [CartEvent]})