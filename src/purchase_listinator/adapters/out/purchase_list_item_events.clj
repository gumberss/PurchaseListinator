(ns purchase-listinator.adapters.out.purchase-list-item-events
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-item :as models.internal.purchase-item]
            [purchase-listinator.wires.out.purchase-list-item-events :as wires.out.purchase-list-item-events]))

(s/defn ->ItemDeletedEvent :- wires.out.purchase-list-item-events/ItemDeletedEvent
  [{:keys [id category-id]} :- models.internal.purchase-item/PurchaseItem
   moment :- s/Num]
  {:item-id     id
   :category-id category-id
   :moment      moment})

(s/defn ->ItemCreatedEvent :- wires.out.purchase-list-item-events/ItemCreatedEvent
  [{:keys [id] :as wire} :- models.internal.purchase-item/PurchaseItem
   moment :- s/Num]
  (-> wire
      (assoc :moment moment
             :item-id id)
      (dissoc :id)))

(s/defn ->ItemChangedEvent :- wires.out.purchase-list-item-events/ItemChangedEvent
  [{:keys [id] :as wire} :- models.internal.purchase-item/PurchaseItem
   moment :- s/Num]
  (-> wire
      (assoc :moment moment
             :item-id id)
      (dissoc :id)))
