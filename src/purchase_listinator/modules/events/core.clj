(ns purchase-listinator.modules.events.core
  (:require [com.stuartsierra.component :as component]
            [purchase-listinator.modules.events.components.rabbitmq :as rabbitmq]
            [purchase-listinator.modules.events.components.datahike :as components.datahike]))

(def rabbitmq-dependencies
  [:config :shopping-events/main-db])
(def webapp-dependencies
  [:shopping-events/rabbitmq :shopping-events/main-db])

(def components
  {:shopping-events/main-db  (component/using (components.datahike/new-datahike :shopping-events/main-db) [:config])
   :shopping-events/rabbitmq (component/using (rabbitmq/new-rabbit-mq :shopping-events/rabbitmq) rabbitmq-dependencies)})

(def system-components-test
  {:shopping-events/main-db  (component/using (components.datahike/new-datahike :shopping-events/main-db) [:config])
   :shopping-events/rabbitmq (component/using (rabbitmq/new-rabbit-mq-fake) rabbitmq-dependencies)})


(def system-config
  {:shopping-events/main-db  {:store (if (System/getenv "DATAHIKE_PATH")
                                       {:backend :file
                                        :path    (or (System/getenv "DATAHIKE_PATH") "")}
                                       {:backend :mem
                                        :id      "shopping-events/database"})}
   :shopping-events/rabbitmq {:host     (or (System/getenv "RABBITMQ_HOST") "localhost")
                              :port     5672
                              :username "guest"
                              :password "guest"
                              :vhost    "/"}})

(defn config-test
  []
  {:shopping-events/main-db  {:store {:backend :mem
                                      :id      (str (random-uuid))}}
   :shopping-events/rabbitmq {}})
(def config
  {:rabbitmq-dependencies rabbitmq-dependencies
   :webapp-dependencies   webapp-dependencies
   :system-components     components
   :system-config         system-config})


(defn system-config-test
  []
  {:system-components system-components-test
   :system-config     (config-test)})