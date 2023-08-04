(ns purchase-listinator.purchase-listinator-core
  (:require [com.stuartsierra.component :as component]
            [purchase-listinator.components.datomic :as datomic]
            [purchase-listinator.components.mongo :as mongo]
            [purchase-listinator.components.rabbitmq :as rabbitmq]
            [purchase-listinator.endpoints.http.purchase-list :as http.purchase-list]
            [purchase-listinator.endpoints.http.shopping :as http.shopping]
            [purchase-listinator.endpoints.http.user :as endpoints.http.user]
            [purchase-listinator.components.http :as components.http]))

(def rabbitmq-dependencies
  [:config :datomic :mongo :http])

(def purchase-listinator-components
  {:mongo    (component/using (mongo/new-mongo) [:config])
   :datomic  (component/using (datomic/new-datomic) [:config :service-map])
   :rabbitmq (component/using (rabbitmq/new-rabbit-mq) rabbitmq-dependencies)
   :http     (component/using (components.http/new-http :shopping/request-routes) [:config])})

(def request-routes
  {:price-suggestion/items       (or (System/getenv "PRICE_SUGGESTION_ITEMS_URL") "http://localhost:3000/api/price-suggestion/by/items")
   :purchase-list/allowed-lists  (or (System/getenv "PURCHASE_LIST_URL") "http://localhost:3000/api/lists/allowed")
   :shopping-cart/receive-events (or (System/getenv "SHOPPING_CART_RECEIVE_EVENTS") "http://localhost:3000/api/shopping-cart/events")
   :shopping-cart/init-cart      (or (System/getenv "SHOPPING_CART_INITIATE_CART") "http://localhost:3000/api/shopping-cart/initiate")
   :shopping-cart/cart           (or (System/getenv "SHOPPING_CART_GET_CART") "http://localhost:3000/api/shopping-cart/by/:list-id/:shopping-id")
   :shopping-cart/exclusive-cart (or (System/getenv "SHOPPING_CART_GET_EXCLUSIVE_CART") "http://localhost:3000/api/shopping-cart/exclusive-by/:list-id/:shopping-id")})

(def purchase-listinator-config
  {:mongo                   {:port    27017
                             :host    (or (System/getenv "MONGODB_HOST") "localhost")
                             :db-name "purchase-listinator"
                             :uri     (or (System/getenv "MONGODB_URI") nil)}
   :datomic                 {:store (if (System/getenv "DATAHIKE_PATH")
                                      {:backend :file
                                       :path    (or (System/getenv "DATAHIKE_PATH") "")}
                                      {:backend :mem
                                       :id      "default"})}
   :rabbitmq                {:host     (or (System/getenv "RABBITMQ_HOST") "localhost")
                             :port     5672
                             :username "guest"
                             :password "guest"
                             :vhost    "/"}
   :shopping/request-routes request-routes})

(def system-components-test
  {:datomic  (component/using (datomic/new-datomic) [:config :service-map])
   :rabbitmq (component/using (rabbitmq/new-rabbit-mq-fake) [])
   :http     (component/using (components.http/new-http-mock) [:config])
   ;todo: fake mongo
   :mongo    (component/using (mongo/new-mongo-fake) [:config])})

(defn system-config-test
  []
  {:env        :test
   :web-server {:port 5150
                :host "192.168.1.104"}
   :datomic    {:store {:backend :mem
                        :id      (str (random-uuid))}}
   :mongo      {:port    27017
                :host    (or (System/getenv "MONGODB_HOST") "localhost")
                :db-name "purchase-listinator-test"
                :uri     (or (System/getenv "MONGODB_URI") nil)}})


(def module-config
  {:rabbitmq-dependencies rabbitmq-dependencies
   :webapp-dependencies   [:service-map :mongo :datomic :rabbitmq :http]
   :routes                (set (concat endpoints.http.user/routes
                                       http.purchase-list/routes
                                       http.shopping/routes))
   :system-config         purchase-listinator-config
   :system-components     purchase-listinator-components})

(defn module-config-test
  []
  {:system-config     (system-config-test)
   :system-components system-components-test})