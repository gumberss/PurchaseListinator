(ns purchase-listinator.modules.price-suggestion.core
  (:require [purchase-listinator.modules.price-suggestion.diplomat.http.server :as http.server]))

(def rabbitmq-dependencies
  [:config :price-suggestion/main-db])
(def webapp-dependencies
  [:price-suggestion/rabbitmq :price-suggestion/main-db])

(def components
  {})

(def system-components-test
  {})

(def system-config
  {})

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
