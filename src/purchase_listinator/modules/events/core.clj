(ns purchase-listinator.modules.events.core
  (:require [com.stuartsierra.component :as component]
            [purchase-listinator.modules.events.components.rabbitmq :as rabbitmq]
            [purchase-listinator.modules.events.components.datahike :as components.datahike]))

(def rabbitmq-dependencies
  [:config :events/main-db])
(def webapp-dependencies
  [:events/rabbitmq :events/main-db])

(def components
  {:events/main-db  (component/using (components.datahike/new-datahike :events/main-db) [:config])
   :events/rabbitmq (component/using (rabbitmq/new-rabbit-mq :events/rabbitmq) rabbitmq-dependencies)})

(def system-config
  {:events/main-db  {:store (if (System/getenv "DATAHIKE_PATH")
                              {:backend :file
                               :path    (or (System/getenv "DATAHIKE_PATH") "")}
                              {:backend :mem
                               :id      "events/database"})}
   :events/rabbitmq {:host     (or (System/getenv "RABBITMQ_HOST") "localhost")
                     :port     5672
                     :username "guest"
                     :password "guest"
                     :vhost    "/"}})
(def config
  {:rabbitmq-dependencies rabbitmq-dependencies
   :webapp-dependencies webapp-dependencies
   :system-components components
   :system-config system-config
   :system-config-test system-config})