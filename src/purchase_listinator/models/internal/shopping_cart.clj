(ns purchase-listinator.models.internal.shopping-cart
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-item :as models.internal.shopping-item]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]))

#_(def cart-item-skeleton models.internal.shopping-item/shopping-item-skeleton)
#_(s/defschema CartItem cart-item-skeleton)

(s/defschema OrderCategoryEvent
  {:moment       s/Num
   :event-type   (s/eq :order-category)
   :shopping-id  s/Uuid
   :category-id  s/Uuid
   :old-position s/Int
   :new-position s/Int})

(s/defschema OrderItemEvent
  {:moment       s/Num
   :event-type   (s/eq :order-item)
   :shopping-id  s/Uuid
   :item-id      s/Uuid
   :old-position s/Int
   :new-position s/Int
   :old-category s/Uuid
   :new-category s/Uuid})

(s/defn of-type
  [expected-event-type {:keys [event-type]}]
  (= expected-event-type event-type))

(s/defschema CartEvent
  (s/conditional
    (partial of-type :order-category) OrderCategoryEvent
    (partial of-type :order-item) OrderItemEvent))

(def cart-skeleton
  {:shopping-id s/Uuid
   :events      [CartEvent]})
(s/defschema Cart cart-skeleton)


