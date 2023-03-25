(ns purchase-listinator.modules.events.flows.shopping-events
  (:require
    [purchase-listinator.modules.events.schemas.models.shopping-event :as modules.events.schemas.models.shopping-event]
    [purchase-listinator.modules.events.diplomat.db.shopping-events :as diplomat.db.shopping-events]
    [schema.core :as s]))

(s/defn receive-events
  [{:keys [events]} :- modules.events.schemas.models.shopping-event/ShoppingEventCollection
   {:shopping-events/keys [main-db]}]
  (diplomat.db.shopping-events/upsert events main-db))