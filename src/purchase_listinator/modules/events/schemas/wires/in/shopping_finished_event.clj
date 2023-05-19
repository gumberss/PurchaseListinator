(ns purchase-listinator.modules.events.schemas.wires.in.shopping-finished-event
  (:require
    [purchase-listinator.misc.schema :as misc.schema]
    [purchase-listinator.modules.events.schemas.wires.in.shopping-events :as wires.in.events]))
(misc.schema/loose-schema ShoppingFinishedEvent
  {:events [wires.in.events/Event]})
