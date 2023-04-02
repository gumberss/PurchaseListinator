(ns purchase-listinator.modules.events.flows.retrieve-events
  (:require
    [purchase-listinator.modules.events.diplomat.db.shopping-events :as diplomat.db.shopping-events]
    [purchase-listinator.modules.events.schemas.models.shopping-event :as models.shopping-event]
    [schema.core :as s]))

(s/defn get-items-by-item-id :- models.shopping-event/ShoppingEventCollection
  [item-id :- s/Uuid
   {:shopping-events/keys [main-db]}]
  {:events (diplomat.db.shopping-events/get-by-item-id item-id main-db)})