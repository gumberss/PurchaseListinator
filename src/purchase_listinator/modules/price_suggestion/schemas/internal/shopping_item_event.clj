(ns purchase-listinator.modules.price-suggestion.schemas.internal.shopping-item-event
  (:require [schema.core :as s]
            [purchase-listinator.misc.schema :as misc.schema]))

(misc.schema/loose-schema Event
  {:id                     s/Uuid
   :moment                 s/Num
   :event-type             s/Keyword
   :shopping-id            s/Uuid
   (s/optional-key :price) s/Num})

(s/defschema ShoppingItemEvent
  {:item-id s/Uuid
   :events  [Event]})

(s/defschema ShoppingItemEvents
  [ShoppingItemEvent])

