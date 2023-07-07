(ns purchase-listinator.modules.shopping-cart.adapters.in.purchase-list-events
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping-cart.schemas.wire.in.purchase-list-category-events :as wire.in.purchase-list-category-events]
            [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.purchase-list-category-events]))

(s/defn category-created-event->internal :- internal.purchase-list-category-events/PurchaseListCategoryCreated
  [{:keys [event-id] :as wire} :- wire.in.purchase-list-category-events/PurchaseCategoryCreatedEvent]
  (-> (assoc wire :event-type :purchase-list-category-created
                  :id event-id)
      (dissoc :event-id)))

(s/defn category-deleted-event->internal :- internal.purchase-list-category-events/PurchaseListCategoryDeleted
  [{:keys [event-id] :as wire} :- wire.in.purchase-list-category-events/PurchaseCategoryDeletedEvent]
  (-> (assoc wire :event-type :purchase-list-category-deleted
                  :id event-id)
      (dissoc :event-id)))