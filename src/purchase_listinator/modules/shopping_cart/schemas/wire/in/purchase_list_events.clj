(ns purchase-listinator.modules.shopping-cart.schemas.wire.in.purchase-list-events
  (:require
    [purchase-listinator.misc.schema :as misc.schema]
    [schema.core :as s]
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.purchase-list-category-events]))

(misc.schema/loose-schema PurchaseCategoryCreatedEvent
  (-> (assoc internal.purchase-list-category-events/purchase-list-categoty-created-skeleton :event-id s/Uuid)
      (dissoc :id :event-type)))

(misc.schema/loose-schema PurchaseCategoryDeletedEvent
  (-> (assoc internal.purchase-list-category-events/purchase-list-category-deleted-skeleton :event-id s/Uuid)
      (dissoc :id :event-type)))

(misc.schema/loose-schema PurchaseItemCreatedEvent
  (-> (assoc internal.purchase-list-category-events/purchase-list-item-created-skeleton :event-id s/Uuid)
      (dissoc :id :event-type)))

(misc.schema/loose-schema PurchaseItemDeletedEvent
  (-> (assoc internal.purchase-list-category-events/purchase-item-deleted-skeleton :event-id s/Uuid)
      (dissoc :id :event-type)))
