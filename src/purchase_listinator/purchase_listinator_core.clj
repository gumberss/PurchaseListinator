(ns purchase-listinator.purchase-listinator-core
  (:require [com.stuartsierra.component :as component]
            [purchase-listinator.components.datomic :as datomic]
            [purchase-listinator.components.rabbitmq :as rabbitmq]
            [purchase-listinator.endpoints.http.purchase-list :as http.purchase-list]
            [purchase-listinator.endpoints.http.user :as endpoints.http.user]
            [purchase-listinator.components.http :as components.http]))

(def rabbitmq-dependencies
  [:config :datomic :http])

(def purchase-listinator-components
  {:datomic  (component/using (datomic/new-datomic) [:config :service-map])
   :rabbitmq (component/using (rabbitmq/new-rabbit-mq) rabbitmq-dependencies)
   :http     (component/using (components.http/new-http :shopping/request-routes) [:config])})

(def request-routes
  {:purchase-list/allowed-lists  (or (System/getenv "PURCHASE_LIST_URL") "http://localhost:3000/api/lists/allowed")})

(def purchase-listinator-config
  {:datomic                 {:store (if (System/getenv "DATAHIKE_PATH")
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
   :http     (component/using (components.http/new-http-mock) [:config])})

(defn system-config-test
  []
  {:env        :test
   :web-server {:port 5150
                :host "192.168.1.104"}
   :datomic    {:store {:backend :mem
                        :id      (str (random-uuid))}}})


(def module-config
  {:rabbitmq-dependencies rabbitmq-dependencies
   :webapp-dependencies   [:service-map :datomic :rabbitmq :http]
   :routes                (set (concat endpoints.http.user/routes
                                       http.purchase-list/routes))
   :system-config         purchase-listinator-config
   :system-components     purchase-listinator-components})

(defn module-config-test
  []
  {:system-config     (system-config-test)
   :system-components system-components-test})