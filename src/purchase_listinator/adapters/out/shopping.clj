(ns purchase-listinator.adapters.out.shopping
  (:require [schema.core :as s]
            [purchase-listinator.wires.out.shopping :as wires.out.shopping]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]))

(s/defn ->ShoppingFinishedEvent :- wires.out.shopping/ShoppingFinishedEvent
  [{:keys [user-id] :as shopping} :- models.internal.shopping/Shopping
   moment :- s/Num
   event-id :- s/Uuid]
  {:event-id event-id
   :user-id user-id
   :moment   moment
   :shopping shopping})
