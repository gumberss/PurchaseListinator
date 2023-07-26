(ns purchase-listinator.modules.events.flows.receive-shopping-events
  (:require
    [purchase-listinator.modules.events.schemas.models.shopping-events :as modules.events.schemas.models.shopping-event]
    [purchase-listinator.modules.events.diplomat.db.shopping-events :as diplomat.db.shopping-events]
    [purchase-listinator.modules.events.schemas.models.cart-events :as models.cart-events]
    [schema.core :as s]))

(s/defn receive-events
  [{:keys [events]} :- modules.events.schemas.models.shopping-event/ShoppingEventCollection
   {:shopping-events/keys [main-db]}]
  (diplomat.db.shopping-events/upsert events main-db))

(s/defn receive-cart-events
  [{:keys [events]} :- models.cart-events/CartEventCollection
   {:shopping-events/keys [main-db]}]
  (diplomat.db.shopping-events/upsert events main-db))