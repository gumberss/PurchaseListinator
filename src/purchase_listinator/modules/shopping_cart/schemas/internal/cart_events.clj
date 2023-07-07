(ns purchase-listinator.modules.shopping-cart.schemas.internal.cart-events
  (:require [schema.core :as s]))


(s/defschema PurchaseListCategoryCreated
  {:id                           s/Uuid
   :moment                       s/Num
   :event-type                   (s/eq :purchase-list-category-created)
   :user-id                      s/Uuid
   ;(s/optional-key :shopping-id) s/Uuid
   :name                         s/Str
   :category-id                  s/Uuid
   :order-position               s/Int
   :color                        s/Int
   :purchase-list-id             s/Uuid})

(s/defn of-type
  [expected-event-type {:keys [event-type]}]
  (= expected-event-type event-type))

(s/defschema CartEvent
  (s/conditional
    (partial of-type :purchase-list-category-created) PurchaseListCategoryCreated
    ;(partial of-type :reorder-category) ReorderCategoryEvent
    ;(partial of-type :reorder-item) ReorderItemEvent
    ;(partial of-type :purchase-list-category-deleted) PurchaseListCategoryDeleted
    ;(partial of-type :purchase-list-item-created) PurchaseListItemCreated
    ;(partial of-type :purchase-list-item-changed) PurchaseListItemChanged
    ;(partial of-type :purchase-list-item-deleted) PurchaseListItemDeleted
    ;(partial of-type :item-price-suggested) ItemPriceSuggested
    ))