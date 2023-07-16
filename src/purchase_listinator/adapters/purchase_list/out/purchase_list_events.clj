(ns purchase-listinator.adapters.purchase-list.out.purchase-list-events
  (:require [schema.core :as s]
            [purchase-listinator.wires.purchase-list.out.purchase-list-events :as out.purchase-list-events]))

(s/defn ->PurchaseListDisabledEvent :- out.purchase-list-events/ListDisabledEvent
  [list-id :- s/Uuid
   user-id :- s/Uuid
   moment :- s/Num
   event-id :- s/Uuid]
  {:purchase-list-id list-id
   :user-id          user-id
   :moment           moment
   :event-id         event-id})