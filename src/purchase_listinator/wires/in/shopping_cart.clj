(ns purchase-listinator.wires.in.shopping-cart
  (:require [schema.core :as s]))

(s/defschema OrderCategoryEvent
             {:moment       s/Num
              :event-type   (s/enum :order-category)
              :old-position s/Int
              :new-position s/Int})

(s/defschema OrderItemEvent
             {:moment       s/Num
              :event-type   s/Keyword
              :old-position s/Int
              :new-position s/Int
              :old-category s/Uuid
              :new-category s/Uuid})
