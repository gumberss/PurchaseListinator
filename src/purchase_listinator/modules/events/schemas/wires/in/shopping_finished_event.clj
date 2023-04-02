(ns purchase-listinator.modules.events.schemas.wires.in.shopping-finished-event
  (:require [schema.core :as s]
            [purchase-listinator.modules.events.schemas.wires.in.shopping-events :as wires.in.events]))
(s/defschema ShoppingFinishedEvent
  {:events   [wires.in.events/Event]})
