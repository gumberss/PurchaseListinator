(ns purchase-listinator.modules.price-suggestion.diplomat.http.client
  (:require
    [purchase-listinator.modules.price-suggestion.components.http :as components.http]
    [schema.core :as s]))

(s/defn get-items-events
  [items-ids :- [s/Uuid]
   user-id :- s/Uuid
   http]
  (components.http/request http {:method :get
                                 :url :shopping-events/get-events-by-items
                                 :query-params {:items-ids items-ids}
                                 :user-id user-id}))