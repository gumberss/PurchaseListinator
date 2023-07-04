(ns purchase-listinator.modules.shopping-cart.schemas.wire.purchase-list
  (:require
    [purchase-listinator.misc.schema :as misc.schema]
    [purchase-listinator.modules.shopping-cart.schemas.internal.purchase-list :as modules.shopping-cart.schemas.internal.purchase-list]))

(misc.schema/loose-schema Item
  modules.shopping-cart.schemas.internal.purchase-list/item-skeleton)

(misc.schema/loose-schema Category
  (assoc modules.shopping-cart.schemas.internal.purchase-list/category-skeleton
    :items [Item]))

(misc.schema/loose-schema PurchaseList
  (assoc modules.shopping-cart.schemas.internal.purchase-list/purchase-list-skeleton
    :categories [Category]))