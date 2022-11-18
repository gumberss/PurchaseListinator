(ns purchase-listinator.models.internal.shopping-cart
  (:require [schema.core :as s]))

(s/defschema ReorderCategoryEvent
  {:moment       s/Num
   :event-type   (s/eq :reorder-category)
   :shopping-id  s/Uuid
   :category-id  s/Uuid
   :new-position s/Int})

(s/defschema ReorderItemEvent
  {:moment          s/Num
   :event-type      (s/eq :reorder-item)
   :shopping-id     s/Uuid
   :item-id         s/Uuid
   :new-position    s/Int
   :new-category-id s/Uuid})

(s/defschema ChangeItemEvent
  {:moment           s/Num
   :event-type       (s/eq :change-item)
   :shopping-id      s/Uuid
   :item-id          s/Uuid
   :price            s/Num
   :quantity-changed s/Int})

(s/defschema PurchaseListCategoryDeleted
  {:moment           s/Num
   :event-type       (s/eq :purchase-list-category-deleted)
   :category-id      s/Uuid
   :purchase-list-id s/Uuid})

(s/defschema PurchaseListCategoryCreated
  {:moment           s/Num
   :event-type       (s/eq :purchase-list-category-created)
   :name             s/Str
   :category-id      s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid})

(s/defn of-type
  [expected-event-type {:keys [event-type]}]
  (= expected-event-type event-type))

(s/defschema CartEvent
  (s/conditional
    (partial of-type :reorder-category) ReorderCategoryEvent
    (partial of-type :reorder-item) ReorderItemEvent
    (partial of-type :purchase-list-category-deleted) PurchaseListCategoryDeleted
    (partial of-type :reorder-item) PurchaseListCategoryCreated))

(def cart-skeleton
  {:shopping-id s/Uuid
   :events      [CartEvent]})
(s/defschema Cart cart-skeleton)


