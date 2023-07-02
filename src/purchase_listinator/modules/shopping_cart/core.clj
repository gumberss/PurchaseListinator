(ns purchase-listinator.modules.shopping-cart.core
  (:require
    [com.stuartsierra.component :as component]
    [purchase-listinator.components.redis :as redis]
    [purchase-listinator.modules.shopping-cart.diplomat.http.server :as modules.shopping-cart.diplomat.http.server]))

(def rabbitmq-dependencies
  [:shopping-cart/redis])
(def webapp-dependencies
  [:config :shopping-cart/redis])

(def components
  {:shopping-cart/redis (component/using (redis/new-Redis {:config-key :shopping-cart/redis}) [:config])})

(def system-components-test
  {:shopping-cart/redis    (component/using (redis/new-redis-mock {:config-key :shopping-cart/redis}) [:config])})

(def request-routes
  {})

(def system-config
  {:shopping-cart/request-routes request-routes
   :shopping-cart/redis          {:host     (or (System/getenv "SHOPPING_CART_REDIS_HOST") "localhost")
                                  :port     (or (System/getenv "SHOPPING_CART_REDIS_PORT") 6380)
                                  :username (or (System/getenv "SHOPPING_CART_REDIS_USERNAME") nil)
                                  :password (or (System/getenv "SHOPPING_CART_REDIS_PASSWORD") "pass")
                                  :timeout  6000}})

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
