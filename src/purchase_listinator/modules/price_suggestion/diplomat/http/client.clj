(ns purchase-listinator.modules.price-suggestion.diplomat.http.client
  (:require
    [purchase-listinator.components.http :as components.http]
    [purchase-listinator.modules.price-suggestion.schemas.internal.shopping-item-event :as internal.shopping-item-event]
    [purchase-listinator.modules.price-suggestion.schemas.wire.in.shopping-item-event :as wire.in.shopping-item-event]
    [purchase-listinator.modules.price-suggestion.adapters.in.shopping-item-event :as adapters.in.shopping-item-event]
    [schema.core :as s]))

(s/defn get-items-events :- internal.shopping-item-event/ShoppingItemEvents
  [items-ids :- [s/Uuid]
   user-id :- s/Uuid
   http]
  (-> (components.http/request http {:method        :get
                                     :url           :shopping-events/get-events-by-items
                                     :query-params  {:items-ids items-ids}
                                     :user-id       user-id
                                     :result-schema wire.in.shopping-item-event/ShoppingItemEventsResult})
      (adapters.in.shopping-item-event/wire->internal)))