(ns purchase-listinator.modules.events.logic.shopping-item-events
  (:require
    [purchase-listinator.modules.events.schemas.models.shopping-events :as schemas.models.shopping-event]
    [purchase-listinator.modules.events.schemas.models.shopping-item-events :as models.shopping-item-event]
    [schema.core :as s]))

(s/defn ->shopping-events-collections
  [[item-id events]]
  {:item-id item-id
   :events  events})
(s/defn ->shopping-events-collections-by-item :- [models.shopping-item-event/ShoppingCartItemEventCollection]
  [events :- [schemas.models.shopping-event/ShoppingEvent]]
  (->> (group-by :item-id events)
       (map ->shopping-events-collections)))