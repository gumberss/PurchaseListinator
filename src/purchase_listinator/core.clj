(ns purchase-listinator.core
  (:require [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl
             :refer [reset set-init start stop system]]
            [io.pedestal.http :as http]
            [purchase-listinator.components.pedestal :as pedestal]
            [purchase-listinator.components.mongo :as mongo]
            [purchase-listinator.components.datomic :as datomic]))

(defn new-system
  [config]
  (component/system-map
    :mongo (component/using
             (mongo/new-mongo)
             [:config])
    :datomic (component/using
               (datomic/new-datomic)
               [:config])
    :config config
    :service-map {:env         (:env config)
                  ::http/type  :jetty
                  ::http/port  (get-in config [:web-server :port])
                  ::http/join? false}
    :pedestal (component/using
                (pedestal/new-pedestal)
                [:service-map :mongo :datomic])))

(def system-config
  {:env :prod
   :web-server {:port 8890
                :host "localhost"}
   :mongo      {:port 27017
                :host "localhost"
                :db-name "monger-test"}
   :datomic    {:db-uri "datomic:free://localhost:4334/datomic-component?password=datomic"}})


(set-init (constantly (new-system system-config)))