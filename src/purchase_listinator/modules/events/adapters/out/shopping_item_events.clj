(ns purchase-listinator.modules.events.adapters.out.shopping-item-events
  (:require
    [purchase-listinator.modules.events.schemas.models.shopping-item-events :as models.shopping-item-events]
    [purchase-listinator.modules.events.schemas.wires.out.http.shopping-item-events :as wires.out.http.shopping-item-events]
    [purchase-listinator.modules.events.adapters.out.shopping-events :as adapters.out.shopping-events]
    [schema.core :as s]))

(s/defn internal->wire :- wires.out.http.shopping-item-events/ShoppingItemEventCollection
  [{:keys [item-id events]} :- models.shopping-item-events/ShoppingItemEventCollection]
  {:item-id item-id
   :events  (map adapters.out.shopping-events/->wire events)})

(s/defn internal-collections->wire-result :- wires.out.http.shopping-item-events/ShoppingItemEventsResult
  [collections :- [models.shopping-item-events/ShoppingItemEventCollection]]
  {:events-collections (map internal->wire collections)})