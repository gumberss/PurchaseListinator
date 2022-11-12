(ns purchase-listinator.core
  (:require [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl
             :refer [reset set-init start stop system]]
            [io.pedestal.http :as http]
            [purchase-listinator.components.pedestal :as pedestal]
            [purchase-listinator.components.mongo :as mongo]
            [purchase-listinator.components.redis :as redis]
            [purchase-listinator.components.datomic :as datomic]
            [purchase-listinator.components.rabbitmq :as rabbitmq]))

(defn new-system
  [config]
  (component/system-map
    :config config
    :service-map {:env         (:env config)
                  ::http/type  :jetty
                  ::http/port  (get-in config [:web-server :port])
                  ::http/host (get-in config [:web-server :host])
                  ::http/join? false}
    :redis (component/using (redis/new-Redis) [:config])
    :mongo (component/using (mongo/new-mongo) [:config])
    :datomic (component/using (datomic/new-datomic) [:config])
    :pedestal (component/using (pedestal/new-pedestal) [:service-map :redis :mongo :datomic])
    :rabbitmq (component/using (rabbitmq/new-rabbit-mq) [:config])))

; Put this configs in the .env file
(def system-config
  {:env        :prod
   :web-server {:port 5150
                :host "192.168.1.102"}
   :mongo      {:port    27017
                :host    "localhost"
                :db-name "monger-test"}
   :datomic    {:db-uri "datomic:free://localhost:4334/datomic-component?password=datomic"}
   :redis      {:host     "127.0.0.1"
                :port     6379
                :password "pass"
                :timeout  6000}
   :rabbitmq {:host "localhost"
              :port 5672
              :username "guest"
              :password "guest"
              :vhost "/"}})


(set-init (constantly (new-system system-config)))
