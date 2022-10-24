(ns purchase-listinator.models.internal.shopping-cart
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-item :as models.internal.shopping-item]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]))

#_(def cart-item-skeleton models.internal.shopping-item/shopping-item-skeleton)
#_(s/defschema CartItem cart-item-skeleton)

(s/defschema ReorderCategoryEvent
  {:moment       s/Num
   :event-type   (s/eq :reorder-category)
   :shopping-id  s/Uuid
   :category-id  s/Uuid
   :new-position s/Int})

(s/defschema ReorderItemEvent
  {:moment       s/Num
   :event-type   (s/eq :reorder-item)
   :shopping-id  s/Uuid
   :item-id      s/Uuid
   :new-position s/Int
   :new-category-id s/Uuid})

(s/defn of-type
  [expected-event-type {:keys [event-type]}]
  (= expected-event-type event-type))

(s/defschema CartEvent
  (s/conditional
    (partial of-type :reorder-category) ReorderCategoryEvent
    (partial of-type :reorder-item) ReorderItemEvent))

(def cart-skeleton
  {:shopping-id s/Uuid
   :events      [CartEvent]})
(s/defschema Cart cart-skeleton)


