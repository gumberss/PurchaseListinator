(ns purchase-listinator.wires.in.shopping-cart
  (:require [schema.core :as s]))

(s/defschema ReorderCategoryEvent
  {:event-type   s/Str
   :shopping-id  s/Str
   :category-id  s/Str
   :old-position s/Int
   :new-position s/Int})

(s/defschema ReorderItemEvent
  {:event-type   s/Str
   :shopping-id  s/Str
   :item-id      s/Str
   :old-position s/Int
   :new-position s/Int
   :old-category s/Str
   :new-category s/Str})
