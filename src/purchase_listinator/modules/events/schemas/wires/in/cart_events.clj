(ns purchase-listinator.modules.events.schemas.wires.in.cart-events
  (:require
    [purchase-listinator.misc.schema :as misc.schema]
    [schema.core :as s]))

(misc.schema/loose-schema CartEvent
  {:id                           s/Uuid
   :moment                       s/Num
   :event-type                   s/Keyword
   :user-id                      s/Uuid
   (s/optional-key :shopping-id) s/Uuid
   (s/optional-key :item-id)     s/Uuid
   (s/optional-key :category-id) s/Uuid})

(def shopping-cart-closed-event-skeleton
  {:purchase-list-id s/Uuid
   :shopping-id      s/Uuid
   :cart-events      [CartEvent]})

(misc.schema/loose-schema ShoppingCartClosedEvent
  shopping-cart-closed-event-skeleton)