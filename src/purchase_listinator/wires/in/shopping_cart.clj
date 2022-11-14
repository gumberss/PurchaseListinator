(ns purchase-listinator.wires.in.shopping-cart
  (:require [schema.core :as s]))

(s/defschema ReorderCategoryEvent
  {:event-type   s/Str
   :shopping-id  s/Str
   :category-id  s/Str
   :new-position s/Int})

(s/defschema ReorderItemEvent
  {:event-type      s/Str
   :shopping-id     s/Str
   :item-id         s/Str
   :new-position    s/Int
   :new-category-id s/Str})

(s/defschema ChangeItemEvent
  {:event-type       s/Str
   :shopping-id      s/Str
   :item-id          s/Str
   :price            s/Num
   :quantity-changed s/Int})
