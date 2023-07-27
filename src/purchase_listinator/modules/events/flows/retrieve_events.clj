(ns purchase-listinator.modules.events.flows.retrieve-events
  (:require
    [purchase-listinator.modules.events.diplomat.db.shopping-events :as diplomat.db.shopping-events]
    [purchase-listinator.modules.events.schemas.models.shopping-item-events :as models.shopping-item-events]
    [purchase-listinator.modules.events.logic.shopping-item-events :as logic.shopping-event]
    [schema.core :as s]))

(s/defn get-items-by-item-id :- models.shopping-item-events/ShoppingCartItemEventCollection
  [item-id :- s/Uuid
   {:shopping-events/keys [main-db]}]
  {:item-id item-id
   :events (diplomat.db.shopping-events/get-by-item-id item-id main-db)})

(s/defn get-items-by-items :- [models.shopping-item-events/ShoppingCartItemEventCollection]
  [items-ids :- [s/Uuid]
   {:shopping-events/keys [main-db]}]
  (-> (diplomat.db.shopping-events/get-by-items-ids items-ids main-db)
      (logic.shopping-event/->shopping-events-collections-by-item)))