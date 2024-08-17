(ns purchase-listinator.modules.shopping.schemas.wires.out.shopping-cart-event
  (:require [schema.core :as s]))

(s/defschema ChangeItemEvent
  {:event-id         s/Str
   :event-type       s/Str
   :shopping-id      s/Str
   :item-id          s/Str
   :purchase-list-id s/Str
   :price            s/Num
   :quantity-changed s/Int})