(ns purchase-listinator.modules.shopping-cart.core
  (:require
    [com.stuartsierra.component :as component]
    [purchase-listinator.components.http :as components.http]
    [purchase-listinator.components.redis :as redis]
    [purchase-listinator.modules.shopping-cart.diplomat.http.server :as modules.shopping-cart.diplomat.http.server]
    [purchase-listinator.modules.shopping-cart.diplomat.consumers.purchase-list-events :as consumers.purchase-list-events]
    [purchase-listinator.components.rabbitmq :as components.rabbitmq]))

(def rabbitmq-dependencies
  [:config :shopping-cart/redis :shopping-cart/http])
(def webapp-dependencies
  [:config :shopping-cart/redis :shopping-cart/http])

(def components
  {:shopping-cart/redis    (component/using (redis/new-Redis {:config-key :shopping-cart/redis}) [:config])
   :shopping-cart/http     (component/using (components.http/new-http :shopping-cart/request-routes) [:config])
   :shopping-cart/rabbitmq (component/using (components.rabbitmq/new-rabbit-mq-v2 :shopping-cart/rabbitmq
                                                                                  consumers.purchase-list-events/subscribers) rabbitmq-dependencies)})

(def system-components-test
  {:shopping-cart/redis    (component/using (redis/new-redis-mock {:config-key :shopping-cart/redis}) [:config])
   :shopping-cart/rabbitmq (component/using (components.rabbitmq/new-rabbit-mq-fake) [])
   :shopping-cart/http     (component/using (components.http/new-http-mock) [:config])})

(def request-routes
  {:purchase-list/purchase-list-by-id-simple (or (System/getenv "PURCHASE_LIST_BY_ID_URL") "http://localhost:3000/api/purchases/lists/:id/simple")})

(def system-config
  {:shopping-cart/request-routes request-routes
   :shopping-cart/redis          {:host     (or (System/getenv "SHOPPING_CART_REDIS_HOST") "localhost")
                                  :port     (or (System/getenv "SHOPPING_CART_REDIS_PORT") 6380)
                                  :username (or (System/getenv "SHOPPING_CART_REDIS_USERNAME") nil)
                                  :password (or (System/getenv "SHOPPING_CART_REDIS_PASSWORD") "pass")
                                  :timeout  6000}
   :shopping-cart/rabbitmq       {:host     (or (System/getenv "RABBITMQ_HOST") "localhost")
                                  :port     5672
                                  :username "guest"
                                  :password "guest"
                                  :vhost    "/"}})

(defn config-test
  []
  {})

(def config
  {:webapp-dependencies webapp-dependencies
   :system-components   components
   :system-config       system-config
   :routes              (set (concat modules.shopping-cart.diplomat.http.server/routes))})


(defn system-config-test
  []
  {:system-components system-components-test
   :system-config     (config-test)})
