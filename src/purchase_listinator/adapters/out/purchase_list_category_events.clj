(ns purchase-listinator.adapters.out.purchase-list-category-events
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.wires.purchase-list.out.purchase-list-category-events :as wires.out.purchase-list-category-events]))

(s/defn ->CategoryDeletedEvent :- wires.out.purchase-list-category-events/CategoryDeletedEvent
  [{:keys [id purchase-list-id user-id]} :- models.internal.purchase-category/PurchaseCategory
   moment :- s/Num
   event-id :- s/Uuid]
  {:category-id      id
   :user-id          user-id
   :purchase-list-id purchase-list-id
   :moment           moment
   :event-id         event-id})

(s/defn ->CategoryCreatedEvent :- wires.out.purchase-list-category-events/CategoryCreatedEvent
  [{:keys [id user-id] :as wire} :- models.internal.purchase-category/PurchaseCategory
   moment :- s/Num
   event-id :- s/Uuid]
  (-> wire
      (assoc :moment moment
             :category-id id
             :event-id event-id
             :user-id user-id)
      (dissoc :id)))
