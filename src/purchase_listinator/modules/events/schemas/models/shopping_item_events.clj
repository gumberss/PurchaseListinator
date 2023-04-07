(ns purchase-listinator.modules.events.schemas.models.shopping-item-events
  (:require
    [purchase-listinator.modules.events.schemas.models.shopping-events :as models.shopping-event]
    [schema.core :as s]))

(s/defschema ShoppingItemEventCollection
  (apply conj {:item-id s/Uuid} models.shopping-event/ShoppingEventCollection))