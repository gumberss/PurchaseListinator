(ns purchase-listinator.modules.price-suggestion.core
  (:require [purchase-listinator.modules.price-suggestion.diplomat.http.server :as http.server]))

(def rabbitmq-dependencies
  [])
(def webapp-dependencies
  [:config])

(def components
  {})

(def system-components-test
  {})

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
