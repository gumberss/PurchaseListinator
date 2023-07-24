(ns purchase-listinator.models.internal.shopping-cart
  (:require [schema.core :as s]))

(s/defschema ReorderCategoryEvent
  {:id           s/Uuid
   :moment       s/Num
   :event-type   (s/eq :reorder-category)
   :user-id      s/Uuid
   :shopping-id  s/Uuid
   :category-id  s/Uuid
   :new-position s/Int})

(s/defschema ReorderItemEvent
  {:id              s/Uuid
   :moment          s/Num
   :event-type      (s/eq :reorder-item)
   :user-id         s/Uuid
   :shopping-id     s/Uuid
   :item-id         s/Uuid
   :new-position    s/Int
   :new-category-id s/Uuid})

(s/defschema ChangeItemEvent
  {:id               s/Uuid
   :moment           s/Num
   :event-type       (s/eq :change-item)
   :user-id          s/Uuid
   :shopping-id      s/Uuid
   :item-id          s/Uuid
   :price            s/Num
   :quantity-changed s/Int})

(s/defschema PurchaseListCategoryDeleted
  {:id                           s/Uuid
   :moment                       s/Num
   :event-type                   (s/eq :purchase-list-category-deleted)
   :user-id                      s/Uuid
   (s/optional-key :shopping-id) s/Uuid
   :category-id                  s/Uuid
   :purchase-list-id             s/Uuid})

(s/defschema PurchaseListCategoryCreated
  {:id                           s/Uuid
   :moment                       s/Num
   :event-type                   (s/eq :purchase-list-category-created)
   :user-id                      s/Uuid
   (s/optional-key :shopping-id) s/Uuid
   :name                         s/Str
   :category-id                  s/Uuid
   :order-position               s/Int
   :color                        s/Int
   :purchase-list-id             s/Uuid})

(s/defschema PurchaseListItemCreated
  {:id                           s/Uuid
   :event-type                   (s/eq :purchase-list-item-created)
   :user-id                      s/Uuid
   :moment                       s/Num
   :item-id                      s/Uuid
   (s/optional-key :shopping-id) s/Uuid
   :name                         s/Str
   :quantity                     s/Int
   :order-position               s/Int
   :purchase-list-id s/Uuid
   :category-id                  s/Uuid})

(s/defschema PurchaseListItemChanged
  {:id                           s/Uuid
   :event-type                   (s/eq :purchase-list-item-changed)
   :user-id                      s/Uuid
   :moment                       s/Num
   (s/optional-key :shopping-id) s/Uuid
   :item-id                      s/Uuid
   :name                         s/Str
   :quantity                     s/Int
   :order-position               s/Int
   :category-id                  s/Uuid
   :purchase-list-id s/Uuid})

(s/defschema PurchaseListItemDeleted
  {:id                           s/Uuid
   :event-type                   (s/eq :purchase-list-item-deleted)
   :user-id                      s/Uuid
   :moment                       s/Num
   (s/optional-key :shopping-id) s/Uuid
   :item-id                      s/Uuid
   :category-id                  s/Uuid
   :purchase-list-id s/Uuid})

(s/defschema ItemPriceSuggested
  {:id          s/Uuid
   :event-type  (s/eq :item-price-suggested)
   :user-id     s/Uuid
   :moment      s/Num
   :shopping-id s/Uuid
   :item-id     s/Uuid
   :price       s/Num})

(s/defn of-type
  [expected-event-type {:keys [event-type]}]
  (= expected-event-type event-type))

(s/defschema CartEvent
  (s/conditional
    (partial of-type :reorder-category) ReorderCategoryEvent
    (partial of-type :reorder-item) ReorderItemEvent
    (partial of-type :purchase-list-category-deleted) PurchaseListCategoryDeleted
    (partial of-type :purchase-list-category-created) PurchaseListCategoryCreated
    (partial of-type :purchase-list-item-created) PurchaseListItemCreated
    (partial of-type :purchase-list-item-changed) PurchaseListItemChanged
    (partial of-type :purchase-list-item-deleted) PurchaseListItemDeleted
    (partial of-type :item-price-suggested) ItemPriceSuggested))

(def cart-skeleton
  {:shopping-id s/Uuid
   :events      [CartEvent]})
(s/defschema Cart cart-skeleton)


