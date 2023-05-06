(ns purchase-listinator.modules.price-suggestion.schemas.wire.in.shopping-item-event
  (:require
    [purchase-listinator.misc.schema :as misc.schema]
    [schema.core :as s]
    [purchase-listinator.modules.price-suggestion.schemas.internal.shopping-item-event :as internal.shopping-item-event]))

(misc.schema/loose-schema Event
  {:id                     s/Uuid
   :moment                 s/Num
   :event-type             s/Keyword
   :shopping-id            s/Uuid
   (s/optional-key :price) s/Num})

(misc.schema/loose-schema ShoppingItemEvent
  {:item-id s/Uuid
   :events  [Event]})

(s/defschema ShoppingItemEvents
  [ShoppingItemEvent])

(misc.schema/loose-schema ShoppingItemEventsResult
  {:events-collections ShoppingItemEvents})

