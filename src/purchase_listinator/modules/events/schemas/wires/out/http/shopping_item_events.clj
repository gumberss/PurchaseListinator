(ns purchase-listinator.modules.events.schemas.wires.out.http.shopping-item-events
  (:require [purchase-listinator.modules.events.schemas.wires.out.http.shopping-events :as wires.out.http.shopping-events]
            [schema.core :as s]))

(s/defschema ShoppingItemEventCollection
  (apply conj {:item-id s/Uuid} wires.out.http.shopping-events/EventCollection))

(s/defschema ShoppingItemEventsResult
  {:events-collections [ShoppingItemEventCollection]})