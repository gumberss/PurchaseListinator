(ns purchase-listinator.modules.events.adapters.out.shopping-item-events
  (:require
    [purchase-listinator.modules.events.schemas.models.shopping-item-events :as models.shopping-item-events]
    [purchase-listinator.modules.events.schemas.wires.out.http.shopping-item-events :as wires.out.http.shopping-item-events]
    [purchase-listinator.modules.events.adapters.out.shopping-cart-events :as adapters.out.shopping-cart-events]
    [schema.core :as s]))

(s/defn internal->wire :- wires.out.http.shopping-item-events/ShoppingItemEventCollection
  [{:keys [item-id events]} :- models.shopping-item-events/ShoppingCartItemEventCollection]
  {:item-id item-id
   :events  (map adapters.out.shopping-cart-events/->wire events)})

(s/defn internal-collections->wire-result :- wires.out.http.shopping-item-events/ShoppingItemEventsResult
  [collections :- [models.shopping-item-events/ShoppingCartItemEventCollection]]
  {:events-collections (map internal->wire collections)})