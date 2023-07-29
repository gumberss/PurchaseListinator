(ns purchase-listinator.wires.in.cart
  (:require
    [purchase-listinator.misc.schema :as misc.schema]
    [purchase-listinator.models.internal.shopping.purchase-list :as internal.shopping.purchase-list]
    [schema.core :as s]
    [purchase-listinator.models.internal.cart-events :as internal.cart-events]))

(s/defn of-type
  [expected-event-type {:keys [event-type]}]
  (= expected-event-type event-type))

(s/defschema CartEvent
  (s/conditional
    ;purchase-list actions
    (partial of-type "purchase-list-category-created") internal.cart-events/PurchaseListCategoryCreated
    (partial of-type "purchase-list-category-deleted") internal.cart-events/PurchaseListCategoryDeleted
    (partial of-type "purchase-list-item-created") internal.cart-events/PurchaseListItemCreated
    (partial of-type "purchase-list-item-changed") internal.cart-events/PurchaseListItemChanged
    (partial of-type "purchase-list-item-deleted") internal.cart-events/PurchaseListItemDeleted
    ;start-cart actions
    (partial of-type "item-price-suggested") internal.cart-events/ItemPriceSuggested
    ;shopping-actions
    (partial of-type "reorder-category") internal.cart-events/ReorderCategoryEvent
    (partial of-type "reorder-item") internal.cart-events/ReorderItemEvent
    (partial of-type "change-item") internal.cart-events/ChangeItemEvent))

(misc.schema/loose-schema Cart
  {:purchase-list        internal.shopping.purchase-list/PurchaseList
   :shopping-cart-events [CartEvent]})
