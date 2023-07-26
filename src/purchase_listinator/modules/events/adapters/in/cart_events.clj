(ns purchase-listinator.modules.events.adapters.in.cart-events
  (:require
    [purchase-listinator.modules.events.schemas.wires.in.cart-events :as wires.in.shopping-cart-events]
    [purchase-listinator.modules.events.schemas.models.cart-events :as models.cart-events]
    [schema.core :as s]))

(s/defn ^:private ->internal :- models.cart-events/CartEvent
  [shopping-id :- s/Uuid
   {:keys [id moment event-type user-id item-id category-id] :as wire} :- wires.in.shopping-cart-events/CartEvent]
  {:id          id
   :moment      moment
   :event-type  event-type
   :user-id     user-id
   :shopping-id shopping-id
   :item-id     item-id
   :category-id category-id
   :properties  (dissoc wire :id :moment :event-type :user-id :shopping-id :item-id :category-id)})

(s/defn wire->internal :- models.cart-events/CartEventCollection
  [{:keys [shopping-id cart-events]} :- wires.in.shopping-cart-events/ShoppingCartClosedEvent]
  {:events (map (partial ->internal shopping-id) cart-events)})