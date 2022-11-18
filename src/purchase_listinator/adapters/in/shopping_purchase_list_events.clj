(ns purchase-listinator.adapters.in.shopping-purchase-list-events
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
            [purchase-listinator.wires.in.purchase-category-events :as wires.in.purchase-category-events]))

(s/defn category-deleted-event->internal :- models.internal.shopping-cart/PurchaseListCategoryDeleted
  [wire :- wires.in.purchase-category-events/PurchaseCategoryDeletedEvent]
  (assoc wire :event-type :purchase-list-category-deleted))

(s/defn category-created-event->internal :- models.internal.shopping-cart/PurchaseListCategoryCreated
  [wire :- wires.in.purchase-category-events/PurchaseCategoryCreatedEvent]
  (assoc wire :event-type :purchase-list-category-created))
