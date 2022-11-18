(ns purchase-listinator.adapters.out.purchase-list-category-events
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.wires.out.purchase-list-caegory-events :as wires.out.purchase-list-caegory-events]))

(s/defn ->CategoryDeletedEvent :- wires.out.purchase-list-caegory-events/CategoryDeletedEvent
  [{:keys [id purchase-list-id]} :- models.internal.purchase-category/PurchaseCategory
   moment :- s/Num]
  {:category-id      id
   :purchase-list-id purchase-list-id
   :moment           moment})

(s/defn ->CategoryCreatedEvent :- wires.out.purchase-list-caegory-events/CategoryCreatedEvent
  [{:keys [id] :as wire} :- models.internal.purchase-category/PurchaseCategory
   moment :- s/Num]
  (-> wire
      (assoc :moment moment
             :category-id id)
      (dissoc :id)))
