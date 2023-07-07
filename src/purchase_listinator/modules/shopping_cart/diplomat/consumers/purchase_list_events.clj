(ns purchase-listinator.modules.shopping-cart.diplomat.consumers.purchase-list-events
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping-cart.schemas.wire.in.purchase-list-category-events :as wire.in.purchase-list-category-events]
            [purchase-listinator.modules.shopping-cart.adapters.in.purchase-list-events :as adapters.in.purchase-list-events]
            [purchase-listinator.modules.shopping-cart.flows.cart-events-reception :as flows.cart-events-reception]))

(s/defn purchase-list-category-created-event-received
  [_channel
   _metadata
   components
   event :- wire.in.purchase-list-category-events/PurchaseCategoryCreatedEvent]
  (-> (adapters.in.purchase-list-events/category-created-event->internal event)
      (flows.cart-events-reception/receive-cart-event-by-list components)))

(s/defn purchase-list-category-deleted-event-received
  [_channel
   _metadata
   components
   event :- wire.in.purchase-list-category-events/PurchaseCategoryDeletedEvent]
  (-> (adapters.in.purchase-list-events/category-deleted-event->internal event)
      (flows.cart-events-reception/receive-cart-event-by-list components)))

(def subscribers
  [{:exchange :purchase-listinator/purchase-list.category.created
    :queue    :shopping-cart/shopping-list.category.create
    :schema   wire.in.purchase-list-category-events/PurchaseCategoryCreatedEvent
    :handler  purchase-list-category-created-event-received}
   {:exchange :purchase-listinator/purchase-list.category.deleted
    :queue    :shopping-cart/shopping-list.category.delete
    :schema   wire.in.purchase-list-category-events/PurchaseCategoryDeletedEvent
    :handler  purchase-list-category-deleted-event-received}])

