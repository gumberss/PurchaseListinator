(ns purchase-listinator.modules.shopping.adapters.out.shopping
  (:require
    [schema.core :as s]
    [purchase-listinator.modules.shopping.schemas.models.shopping :as shopping.models.shopping]
    [purchase-listinator.modules.shopping.schemas.wires.out.shopping :as wires.out.shopping]))

(s/defn ->ShoppingFinishedEvent :- wires.out.shopping/ShoppingFinishedEvent
  [{:keys [user-id] :as shopping} :- shopping.models.shopping/Shopping
   moment :- s/Num
   event-id :- s/Uuid]
  {:event-id event-id
   :user-id user-id
   :moment   moment
   :shopping shopping})
