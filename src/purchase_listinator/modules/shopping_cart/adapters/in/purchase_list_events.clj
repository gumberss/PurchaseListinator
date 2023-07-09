(ns purchase-listinator.modules.shopping-cart.adapters.in.purchase-list-events
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping-cart.schemas.wire.in.purchase-list-events :as wire.in.purchase-list-events]
            [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.purchase-list-events]))

(s/defn category-created-event->internal :- internal.purchase-list-events/PurchaseListCategoryCreated
  [{:keys [event-id] :as wire} :- wire.in.purchase-list-events/PurchaseCategoryCreatedEvent]
  (-> (assoc wire :event-type :purchase-list-category-created
                  :id event-id)
      (dissoc :event-id)))

(s/defn category-deleted-event->internal :- internal.purchase-list-events/PurchaseListCategoryDeleted
  [{:keys [event-id] :as wire} :- wire.in.purchase-list-events/PurchaseCategoryDeletedEvent]
  (-> (assoc wire :event-type :purchase-list-category-deleted
                  :id event-id)
      (dissoc :event-id)))

(s/defn item-created-event->internal :- internal.purchase-list-events/PurchaseListItemCreated
  [{:keys [event-id] :as wire} :- wire.in.purchase-list-events/PurchaseItemCreatedEvent]
  (-> (assoc wire :event-type :purchase-list-item-created
                  :id event-id)
      (dissoc :event-id)))

(s/defn item-deleted-event->internal :- internal.purchase-list-events/PurchaseListItemDeleted
  [{:keys [event-id] :as wire} :- wire.in.purchase-list-events/PurchaseItemDeletedEvent]
  (-> (assoc wire :event-type :purchase-list-item-deleted
                  :id event-id)
      (dissoc :event-id)))

(s/defn item-changed-event->internal :- internal.purchase-list-events/PurchaseListItemChanged
  [{:keys [event-id] :as wire} :- wire.in.purchase-list-events/PurchaseItemChangedEvent]
  (-> (assoc wire :event-type :purchase-list-item-changed
                  :id event-id)
      (dissoc :event-id)))