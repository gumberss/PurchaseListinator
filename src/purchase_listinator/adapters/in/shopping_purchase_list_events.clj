(ns purchase-listinator.adapters.in.shopping-purchase-list-events
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
            [purchase-listinator.wires.in.purchase-category-events :as wires.in.purchase-category-events]
            [purchase-listinator.wires.in.purchase-item-events :as wires.in.purchase-item-events]))

(s/defn category-deleted-event->internal :- models.internal.shopping-cart/PurchaseListCategoryDeleted
  [wire :- wires.in.purchase-category-events/PurchaseCategoryDeletedEvent]
  (assoc wire :event-type :purchase-list-category-deleted))

(s/defn category-created-event->internal :- models.internal.shopping-cart/PurchaseListCategoryCreated
  [wire :- wires.in.purchase-category-events/PurchaseCategoryCreatedEvent]
  (assoc wire :event-type :purchase-list-category-created))

(s/defn item-created-event->internal :- models.internal.shopping-cart/PurchaseListItemCreated
  [wire :- wires.in.purchase-item-events/PurchaseItemCreatedEvent]
  (assoc wire :event-type :purchase-list-item-created))

(s/defn item-deleted-event->internal :- models.internal.shopping-cart/PurchaseListItemDeleted
  [wire :- wires.in.purchase-item-events/PurchaseItemDeletedEvent]
  (assoc wire :event-type :purchase-list-item-deleted))

(s/defn item-changed-event->internal :- models.internal.shopping-cart/PurchaseListItemChanged
  [wire :- wires.in.purchase-item-events/PurchaseItemChangedEvent]
  (assoc wire :event-type :purchase-list-item-changed))
