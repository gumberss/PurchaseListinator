(ns purchase-listinator.adapters.in.shopping-purchase-list-events
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
            [purchase-listinator.wires.purchase-list.in.purchase-category-events :as wires.in.purchase-category-events]
            [purchase-listinator.wires.purchase-list.in.purchase-item-events :as wires.in.purchase-item-events]))

(s/defn category-deleted-event->internal :- models.internal.shopping-cart/PurchaseListCategoryDeleted
  [{:keys [event-id] :as wire} :- wires.in.purchase-category-events/PurchaseCategoryDeletedEvent]
  (-> (assoc wire :event-type :purchase-list-category-deleted
                  :id event-id)
      (dissoc :event-id)))

(s/defn category-created-event->internal :- models.internal.shopping-cart/PurchaseListCategoryCreated
  [{:keys [event-id] :as wire} :- wires.in.purchase-category-events/PurchaseCategoryCreatedEvent]
  (-> (assoc wire :event-type :purchase-list-category-created
                  :id event-id)
      (dissoc :event-id)))

(s/defn item-created-event->internal :- models.internal.shopping-cart/PurchaseListItemCreated
  [{:keys [event-id] :as wire} :- wires.in.purchase-item-events/PurchaseItemCreatedEvent]
  (-> (assoc wire :event-type :purchase-list-item-created
                  :id event-id)
      (dissoc :event-id)))

(s/defn item-deleted-event->internal :- models.internal.shopping-cart/PurchaseListItemDeleted
  [{:keys [event-id] :as wire} :- wires.in.purchase-item-events/PurchaseItemDeletedEvent]
  (-> (assoc wire :event-type :purchase-list-item-deleted
                  :id event-id)
      (dissoc :event-id)))

(s/defn item-changed-event->internal :- models.internal.shopping-cart/PurchaseListItemChanged
  [{:keys [event-id] :as wire} :- wires.in.purchase-item-events/PurchaseItemChangedEvent]
  (-> (assoc wire :event-type :purchase-list-item-changed
                  :id event-id)
      (dissoc :event-id)))
