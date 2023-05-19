(ns purchase-listinator.wires.purchase-list.in.shopping
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list.shopping :as models.internal.purchase-list.shopping]
            [purchase-listinator.misc.schema :as misc.schema]))

(misc.schema/loose-schema ShoppingItem
  models.internal.purchase-list.shopping/shopping-item-skeleton)

(misc.schema/loose-schema ShoppingCategory
  (assoc models.internal.purchase-list.shopping/shopping-category-skeleton
    :items [ShoppingItem]))

(misc.schema/loose-schema Shopping
  (assoc models.internal.purchase-list.shopping/shopping-skeleton
    (s/optional-key :categories) [ShoppingCategory]))

(misc.schema/loose-schema ShoppingFinishedEvent
  {:event-id s/Uuid
   :moment   s/Num
   :user-id  s/Uuid
   :shopping Shopping})
