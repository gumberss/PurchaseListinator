(ns purchase-listinator.modules.shopping-cart.schemas.wire.in.shopping-events
  (:require [schema.core :as s]))

(s/defschema ReorderCategoryEvent
  {:event-id         s/Str
   :event-type       s/Str
   :shopping-id      s/Str
   :category-id      s/Str
   :purchase-list-id s/Str
   :new-position     s/Int})

(s/defschema ReorderItemEvent
  {:event-id         s/Str
   :event-type       s/Str
   :shopping-id      s/Str
   :item-id          s/Str
   :purchase-list-id s/Str
   :new-position     s/Int
   :new-category-id  s/Str})

(s/defschema ChangeItemEvent
  {:event-id         s/Str
   :event-type       s/Str
   :shopping-id      s/Str
   :item-id          s/Str
   :purchase-list-id s/Str
   :price            s/Num
   :quantity-changed s/Int})
