(ns purchase-listinator.adapters.out.purchase-list-item-events
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list.purchase-item :as models.internal.purchase-item]
            [purchase-listinator.wires.purchase-list.out.purchase-list-item-events :as wires.out.purchase-list-item-events]))

(s/defn ->ItemDeletedEvent :- wires.out.purchase-list-item-events/ItemDeletedEvent
  [{:keys [id category-id user-id]} :- models.internal.purchase-item/PurchaseItem
   list-id :- s/Uuid
   moment :- s/Num
   event-id :- s/Uuid]
  {:item-id          id
   :purchase-list-id list-id
   :user-id          user-id
   :category-id      category-id
   :moment           moment
   :event-id         event-id})

(s/defn ->ItemCreatedEvent :- wires.out.purchase-list-item-events/ItemCreatedEvent
  [{:keys [id user-id] :as wire} :- models.internal.purchase-item/PurchaseItem
   list-id :- s/Uuid
   moment :- s/Num
   event-id :- s/Uuid]
  (-> wire
      (assoc :moment moment
             :item-id id
             :event-id event-id
             :user-id user-id
             :purchase-list-id list-id)
      (dissoc :id)))

(s/defn ->ItemChangedEvent :- wires.out.purchase-list-item-events/ItemChangedEvent
  [{:keys [id user-id] :as wire} :- models.internal.purchase-item/PurchaseItem
   list-id :- s/Uuid
   moment :- s/Num
   event-id :- s/Uuid]
  (-> wire
      (assoc :moment moment
             :item-id id
             :event-id event-id
             :user-id user-id
             :purchase-list-id list-id)
      (dissoc :id)))
