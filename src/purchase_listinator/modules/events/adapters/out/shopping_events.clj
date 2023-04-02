(ns purchase-listinator.modules.events.adapters.out.shopping-events
  (:require
    [purchase-listinator.misc.general :as misc.general]
    [purchase-listinator.modules.events.schemas.models.shopping-event :as models.shopping-event]
    [purchase-listinator.modules.events.schemas.wires.out.http.shopping-events :as wires.out.http.shopping-events]
    [schema.core :as s]))


(s/defn ^:private ->wire :- wires.out.http.shopping-events/Event
  [{:keys [properties] :as wire} :- models.shopping-event/ShoppingEvent]
  (-> (apply assoc wire (flatten (vec properties)))
      (dissoc :properties)
      (misc.general/dissoc-if :item-id nil?)
      (misc.general/dissoc-if :category-id nil?)))
(s/defn internal->wire :- models.shopping-event/ShoppingEventCollection
  [{:keys [events]} :- wires.out.http.shopping-events/EventCollection]
  {:events (map ->wire events)})