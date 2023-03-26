(ns purchase-listinator.purchase-listinator-core
  (:require [com.stuartsierra.component :as component]
            [purchase-listinator.components.datomic :as datomic]
            [purchase-listinator.components.mongo :as mongo]
            [purchase-listinator.components.rabbitmq :as rabbitmq]
            [purchase-listinator.components.redis :as redis]
            [purchase-listinator.endpoints.http.purchase-list :as http.purchase-list]
            [purchase-listinator.endpoints.http.shopping :as http.shopping]
            [purchase-listinator.endpoints.http.user :as endpoints.http.user]))

(def purchase-listinator-components
  {:redis    (component/using (redis/new-Redis) [:config])
   :mongo    (component/using (mongo/new-mongo) [:config])
   :datomic  (component/using (datomic/new-datomic) [:config :service-map])
   :rabbitmq (component/using (rabbitmq/new-rabbit-mq) [:config :redis :datomic :mongo])})

(def purchase-listinator-config
  {:mongo    {:port    27017
              :host    (or (System/getenv "MONGODB_HOST") "localhost")
              :db-name "purchase-listinator"
              :uri     (or (System/getenv "MONGODB_URI") nil)}
   :datomic  {:store (if (System/getenv "DATAHIKE_PATH")
                       {:backend :file
                        :path    (or (System/getenv "DATAHIKE_PATH") "")}
                       {:backend :mem
                        :id      "default"})}
   :redis    {:host     (or (System/getenv "REDIS_HOST") "localhost")
              :port     (or (System/getenv "REDIS_PORT") 6379)
              :username (or (System/getenv "REDIS_USERNAME") nil)
              :password (or (System/getenv "REDIS_PASSWORD") "pass")
              :timeout  6000}
   :rabbitmq {:host     (or (System/getenv "RABBITMQ_HOST") "localhost")
              :port     5672
              :username "guest"
              :password "guest"
              :vhost    "/"}})

(def system-components-test
  {:datomic  (component/using (datomic/new-datomic) [:config :service-map])
   :redis    (component/using (redis/new-redis-mock) [:config])
   :rabbitmq (component/using (rabbitmq/new-rabbit-mq-fake) [])
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
  {:rabbitmq-dependencies [:config :redis :datomic :mongo]
   :webapp-dependencies   [:service-map :mongo :redis :datomic :rabbitmq]
   :routes                (set (concat endpoints.http.user/routes
                                       http.purchase-list/routes
                                       http.shopping/routes))
   :system-config         purchase-listinator-config
   :system-components     purchase-listinator-components})

(defn module-config-test
  []
  {:system-config     (system-config-test)
   :system-components system-components-test})