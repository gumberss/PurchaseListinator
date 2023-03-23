(ns purchase-listinator.purchase-listinator-core
  (:require [com.stuartsierra.component :as component]
            [purchase-listinator.components.datomic :as datomic]
            [purchase-listinator.components.mongo :as mongo]
            [purchase-listinator.components.rabbitmq :as rabbitmq]
            [purchase-listinator.components.redis :as redis]))

(def purchase-listinator-components
  {:redis    (component/using (redis/new-Redis) [:config])
   :mongo    (component/using (mongo/new-mongo) [:config])
   :datomic  (component/using (datomic/new-datomic) [:config :service-map])
   :rabbitmq (component/using (rabbitmq/new-rabbit-mq) [:config :redis :datomic :mongo])})

(def purchase-listinator-config
  {:mongo      {:port    27017
                :host    (or (System/getenv "MONGODB_HOST") "localhost")
                :db-name "purchase-listinator"
                :uri     (or (System/getenv "MONGODB_URI") nil)}
   :datomic    {:store (if (System/getenv "DATAHIKE_PATH")
                         {:backend :file
                          :path    (or (System/getenv "DATAHIKE_PATH") "")}
                         {:backend :mem
                          :id      "default"})}
   :redis      {:host     (or (System/getenv "REDIS_HOST") "localhost")
                :port     (or (System/getenv "REDIS_PORT") 6379)
                :username (or (System/getenv "REDIS_USERNAME") nil)
                :password (or (System/getenv "REDIS_PASSWORD") "pass")
                :timeout  6000}
   :rabbitmq   {:host     (or (System/getenv "RABBITMQ_HOST") "localhost")
                :port     5672
                :username "guest"
                :password "guest"
                :vhost    "/"}})

(def module-config
  {:rabbitmq-dependencies [:config :redis :datomic :mongo]
   :webapp-dependencies   [:service-map :mongo :redis :datomic :rabbitmq]
   :system-config         purchase-listinator-config
   :system-components     purchase-listinator-components})