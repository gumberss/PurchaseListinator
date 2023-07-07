(ns purchase-listinator.modules.shopping-cart.schemas.wire.in.purchase-list-category-events
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.purchase-list-category-events]))
(def purchase-category-created-event-skeleton
  (-> (assoc internal.purchase-list-category-events/PurchaseListCategoryCreated :event-id s/Uuid)
      (dissoc :id :event-type)))
(s/defschema PurchaseCategoryCreatedEvent purchase-category-created-event-skeleton)
