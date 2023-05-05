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
                                 :query-params items-ids
                                 :user-id user-id}))
;(def kk (component/start (components.http/map->Http {:config {:price-suggestion/request-routes {:a "http://localhost:3000/api/events/by/items"}}})))