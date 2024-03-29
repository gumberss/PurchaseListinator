(ns purchase-listinator.modules.price-suggestion.core
  (:require
    [com.stuartsierra.component :as component]
    [purchase-listinator.modules.price-suggestion.diplomat.http.server :as http.server]
    [purchase-listinator.components.http :as components.http]))

(def rabbitmq-dependencies
  [])
(def webapp-dependencies
  [:config :price-suggestion/http])

(def components
  {:price-suggestion/http (component/using (components.http/new-http :price-suggestion/request-routes) [:config])})

(def system-components-test
  {:price-suggestion/http  (component/using (components.http/new-http-mock) [:config])})

(def request-routes
  {:shopping-events/get-events-by-items (or (System/getenv "SHOPPING_EVENTS_URL") "http://localhost:3000/api/events/by/items")})

(def system-config
  {:price-suggestion/request-routes request-routes})

(defn config-test
  []
  {})

(def config
  {:rabbitmq-dependencies rabbitmq-dependencies
   :webapp-dependencies   webapp-dependencies
   :system-components     components
   :system-config         system-config
   :routes                (set (concat http.server/routes))})


(defn system-config-test
  []
  {:system-components system-components-test
   :system-config     (config-test)})
