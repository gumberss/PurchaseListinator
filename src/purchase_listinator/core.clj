(ns purchase-listinator.core
  (:require [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl
             :refer [reset set-init start stop system]]
            [io.pedestal.http :as http]
            [purchase-listinator.components.pedestal :as pedestal]
            [purchase-listinator.components.mongo :as mongo]
            [purchase-listinator.components.redis :as redis]
            [purchase-listinator.components.datomic :as datomic]
            [purchase-listinator.components.rabbitmq :as rabbitmq])
  (:gen-class))

(defn new-system
  [config]
  (component/system-map
    :config config
    :service-map {:env         (:env config)
                  ::http/type  :jetty
                  ::http/port  (get-in config [:web-server :port])
                  ::http/host  (get-in config [:web-server :host])
                  ::http/join? false}
    :redis (component/using (redis/new-Redis) [:config])
    :mongo (component/using (mongo/new-mongo) [:config])
    :datomic (component/using (datomic/new-datomic) [:config :service-map])
    :pedestal (component/using (pedestal/new-pedestal) [:service-map :redis :mongo :datomic :rabbitmq])
    :rabbitmq (component/using (rabbitmq/new-rabbit-mq) [:config :redis :mongo :datomic])))

(defn new-system-test
  [config]
  (component/system-map
    :config config
    :service-map {:env         (:env config)
                  ::http/type  :jetty
                  ::http/port  (get-in config [:web-server :port])
                  ::http/host  (get-in config [:web-server :host])
                  ::http/join? false}
    :datomic (component/using (datomic/new-datomic) [:config :service-map])
    :redis (component/using (redis/new-redis-mock) [:config])
    :pedestal (component/using (pedestal/new-pedestal) [:service-map :datomic :redis :rabbitmq])
    :rabbitmq (component/using (rabbitmq/new-rabbit-mq-fake) [])))

; Put this configs in the .env file
(def system-config
  {:env        :prod
   :web-server {:port 3000
                :host "localhost"}
   :mongo      {:port    27017
                :host    "127.0.0.1"
                :db-name "purchase-listinator"
                :uri     (or (System/getenv "MONGODB_URI") nil)}
   :datomic    {:db-uri "datomic:free://127.0.0.1:4334/datomic-component?password=datomic"}
   :redis      {:host     (or (System/getenv "REDIS_HOST") "127.0.0.1")
                :port     (or (System/getenv "REDIS_PORT") 6379)
                :username (or (System/getenv "REDIS_USERNAME") nil)
                :password (or (System/getenv "REDIS_PASSWORD") "pass")
                :timeout  6000}
   :rabbitmq   {:host     "127.0.0.1"
                :port     5672
                :username "guest"
                :password "guest"
                :vhost    "/"}})

(println "going!")
(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (println "go!")
  (component/start (new-system system-config)))

#_(set-init (constantly (new-system system-config)))
