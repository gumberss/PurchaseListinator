(ns purchase-listinator.wires.purchase-list.out.purchase-list-events
  (:require [schema.core :as s]))

(s/defschema ListDisabledEvent
  {:event-id         s/Uuid
   :purchase-list-id s/Uuid
   :moment           s/Num
   :user-id          s/Uuid})