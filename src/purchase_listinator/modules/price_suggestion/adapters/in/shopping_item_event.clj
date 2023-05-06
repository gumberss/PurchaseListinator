(ns purchase-listinator.modules.price-suggestion.adapters.in.shopping-item-event
  (:require
    [purchase-listinator.modules.price-suggestion.schemas.internal.shopping-item-event :as internal.shopping-item-event]
    [purchase-listinator.modules.price-suggestion.schemas.wire.in.shopping-item-event :as schemas.wire.in.ShoppingItemEvent]
    [schema.core :as s]))

(s/defn event->internal
  [event]
  (select-keys event [:id :moment :event-type :shopping-id :price]))

(s/defn shopping-event->internal
  [{:keys [item-id events]}]
  {:item-id item-id
   :events (map event->internal events)})

(s/defn wire->internal :- internal.shopping-item-event/ShoppingItemEvents
  [{:keys [events-collections]} :- schemas.wire.in.ShoppingItemEvent/ShoppingItemEventsResult]
  (map shopping-event->internal events-collections))