(ns purchase-listinator.modules.shopping-cart.core
  (:require
    [purchase-listinator.modules.shopping-cart.diplomat.http.server :as modules.shopping-cart.diplomat.http.server]))

(def rabbitmq-dependencies
  [])
(def webapp-dependencies
  [:config])

(def components
  {})

(def system-components-test
  {})

(def request-routes
  {})

(def system-config
  {:shopping-cart/request-routes request-routes})

(defn config-test
  []
  {})

(def config
  {:rabbitmq-dependencies rabbitmq-dependencies
   :webapp-dependencies   webapp-dependencies
   :system-components     components
   :system-config         system-config
   :routes                (set (concat modules.shopping-cart.diplomat.http.server/routes))})


(defn system-config-test
  []
  {:system-components system-components-test
   :system-config     (config-test)})
