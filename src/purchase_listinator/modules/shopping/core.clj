(ns purchase-listinator.modules.shopping.core
  (:require [com.stuartsierra.component :as component]
            [purchase-listinator.components.http :as components.http]
            [purchase-listinator.components.mongo :as components.mongo]
            [purchase-listinator.components.rabbitmq-channel :as components.rabbitmq-channel]
            [purchase-listinator.modules.shopping.diplomat.db.datomic.shopping :as datomic.shopping]
            [purchase-listinator.modules.shopping.diplomat.db.datomic.shopping-item :as datomic.shopping-item]
            [purchase-listinator.modules.shopping.diplomat.db.datomic.shopping-category :as datomic.shopping-category]
            [purchase-listinator.modules.shopping.diplomat.http.server :as http.server]
            [purchase-listinator.modules.shopping.diplomat.db.mongo.shopping-location :as db.mongo.shopping-location]
            [purchase-listinator.components.datahike :as components.datahike]
            [purchase-listinator.components.mongo :as components.mongo]
            [purchase-listinator.components.rabbitmq :as components.rabbitmq]))

(def rabbitmq-dependencies
  [:config :shopping/main-db])
(def webapp-dependencies
  [:shopping/rabbitmq :shopping/rabbitmq-channel :shopping/main-db :shopping/http :shopping/mongo])

(def schemas
  (concat datomic.shopping/schema
          datomic.shopping-item/schema
          datomic.shopping-category/schema))

(def mongo-indexes
  [db.mongo.shopping-location/indexes])

(def components
  {:shopping/http             (component/using (components.http/new-http :shopping/request-routes) [:config])
   :shopping/main-db          (component/using (components.datahike/new-datahike :shopping/main-db schemas) [:config])
   :shopping/rabbitmq-channel (component/using (components.rabbitmq-channel/new-rabbit-mq-channel :shopping/rabbitmq) [:config])
   :shopping/rabbitmq         (component/using (components.rabbitmq/new-rabbit-mq-v2 :shopping/rabbitmq :shopping/rabbitmq-channel []) rabbitmq-dependencies)
   :shopping/mongo            (component/using (components.mongo/new-mongo :shopping/mongo mongo-indexes) [:config])})

(def system-components-test
  {:shopping/http             (component/using (components.http/new-http-mock ) [:config])
   :shopping/main-db  (component/using (components.datahike/new-datahike :shopping/main-db schemas) [:config])
   :shopping/rabbitmq (component/using (components.rabbitmq/new-rabbit-mq-fake) rabbitmq-dependencies)
   :shopping/rabbitmq-channel (component/using (components.rabbitmq-channel/new-rabbit-mq-channel-fake :shopping/rabbitmq) [:config])
   ;todo: fake mongo
   :shopping/mongo    (component/using (components.mongo/new-mongo-fake) [:config])})

(def request-routes
  {:price-suggestion/items       (or (System/getenv "PRICE_SUGGESTION_ITEMS_URL") "http://localhost:3000/api/price-suggestion/by/items")
   :purchase-list/allowed-lists  (or (System/getenv "PURCHASE_LIST_URL") "http://localhost:3000/api/lists/allowed")
   :shopping-cart/receive-events (or (System/getenv "SHOPPING_CART_RECEIVE_EVENTS") "http://localhost:3000/api/shopping-cart/events")
   :shopping-cart/init-cart      (or (System/getenv "SHOPPING_CART_INITIATE_CART") "http://localhost:3000/api/shopping-cart/initiate")
   :shopping-cart/cart           (or (System/getenv "SHOPPING_CART_GET_CART") "http://localhost:3000/api/shopping-cart/by/:list-id/:shopping-id")
   :shopping-cart/exclusive-cart (or (System/getenv "SHOPPING_CART_GET_EXCLUSIVE_CART") "http://localhost:3000/api/shopping-cart/exclusive-by/:list-id/:shopping-id")})


(def system-config
  {:shopping/mongo                   {:port    27017
                             :host    (or (System/getenv "MONGODB_HOST") "localhost")
                             :db-name "purchase-listinator"
                             :uri     (or (System/getenv "MONGODB_URI") nil)}
   :shopping/main-db        {:store (if (System/getenv "DATAHIKE_PATH")
                                      {:backend :file
                                       :path    (or (System/getenv "DATAHIKE_PATH") "")}
                                      {:backend :mem
                                       :id      "shopping/database"})}
   :shopping/rabbitmq       {:host     (or (System/getenv "RABBITMQ_HOST") "localhost")
                             :port     5672
                             :username "guest"
                             :password "guest"
                             :vhost    "/"}
   :shopping/request-routes request-routes})

(defn config-test
  []
  {:shopping/main-db  {:store {:backend :mem
                               :id      (str (random-uuid))}}
   :shopping/rabbitmq {}
   :shopping/mongo      {:port    27017
                :host    (or (System/getenv "MONGODB_HOST") "localhost")
                :db-name "purchase-listinator-test"
                :uri     (or (System/getenv "MONGODB_URI") nil)}})
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