(ns purchase-listinator.modules.price-suggestion.schemas.wire.in.shopping-item-event
  (:require [schema.core :as s]
            [purchase-listinator.modules.price-suggestion.schemas.internal.shopping-item-event :as internal.shopping-item-event]))

(s/defschema ShoppingItemEventsResult
  {:events-collections internal.shopping-item-event/ShoppingItemEvents})
