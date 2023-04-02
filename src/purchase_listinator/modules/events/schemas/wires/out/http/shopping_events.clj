(ns purchase-listinator.modules.events.schemas.wires.out.http.shopping-events
  (:require [purchase-listinator.modules.events.schemas.wires.in.shopping-events :as wires.in.shopping-events]
            [schema.core :as s]))

(s/defschema Event
  wires.in.shopping-events/Event)
(s/defschema EventCollection
  {:events [Event]})