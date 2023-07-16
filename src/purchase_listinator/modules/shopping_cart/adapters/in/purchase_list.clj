(ns purchase-listinator.modules.shopping-cart.adapters.in.purchase-list
  (:require
    [schema.core :as s]
    [purchase-listinator.modules.shopping-cart.schemas.wire.in.purchase-list :as shopping-cart.schemas.wire.in.purchase-list]
    [purchase-listinator.modules.shopping-cart.schemas.internal.purchase-list :as shopping-cart.schemas.internal.purchase-list]))

(s/defn wire-items->internal :- shopping-cart.schemas.internal.purchase-list/Item
  [item :- shopping-cart.schemas.wire.in.purchase-list/Item]
  (select-keys item (keys shopping-cart.schemas.internal.purchase-list/item-skeleton)))

(s/defn category-wire->internal :- shopping-cart.schemas.internal.purchase-list/Category
  [{:keys [items] :as category}] :- shopping-cart.schemas.wire.in.purchase-list/Category
  (-> (assoc category :items (map wire-items->internal items))
      (select-keys (keys shopping-cart.schemas.internal.purchase-list/category-skeleton))))

(s/defn purchase-list-wire->internal :- shopping-cart.schemas.internal.purchase-list/PurchaseList
  [{:keys [categories] :as purchase-list} :- shopping-cart.schemas.wire.in.purchase-list/PurchaseList]
  (-> (assoc purchase-list :categories (map category-wire->internal categories))
      (select-keys (keys shopping-cart.schemas.internal.purchase-list/purchase-list-skeleton))))
