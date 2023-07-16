(ns purchase-listinator.modules.shopping-cart.schemas.wire.in.cart-events
  (:require
    [purchase-listinator.misc.schema :as misc.schema]
    [schema.core :as s]
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]))

(misc.schema/loose-schema PurchaseCategoryCreatedEvent
  (-> (assoc internal.cart-events/purchase-list-categoty-created-skeleton :event-id s/Uuid)
      (dissoc :id :event-type)))

(misc.schema/loose-schema PurchaseCategoryDeletedEvent
  (-> (assoc internal.cart-events/purchase-list-category-deleted-skeleton :event-id s/Uuid)
      (dissoc :id :event-type)))

(misc.schema/loose-schema PurchaseItemCreatedEvent
  (-> (assoc internal.cart-events/purchase-list-item-created-skeleton :event-id s/Uuid)
      (dissoc :id :event-type)))

(misc.schema/loose-schema PurchaseItemDeletedEvent
  (-> (assoc internal.cart-events/purchase-item-deleted-skeleton :event-id s/Uuid)
      (dissoc :id :event-type)))

(misc.schema/loose-schema PurchaseItemChangedEvent
  (-> (assoc internal.cart-events/purchase-item-changed-skeleton :event-id s/Uuid)
      (dissoc :id :event-type)))