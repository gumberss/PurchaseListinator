(ns purchase-listinator.core
  (:require
    [clojure.set :as set]
    [com.stuartsierra.component :as component]
    [com.stuartsierra.component.repl
     :refer [reset set-init start stop system]]
    [io.pedestal.http :as http]
    [purchase-listinator.components.pedestal :as pedestal]
    [purchase-listinator.modules.events.core :as modules.events.core]
    [purchase-listinator.purchase-listinator-core :as purchase-listinator-core])
  (:gen-class))

(def modules-config [purchase-listinator-core/module-config
                     modules.events.core/config])
(def system-config
  (reduce conj
          {:env        (keyword (or (System/getenv "ENVIRONMENT_TYPE") "dev"))
           :web-server {:port (or (System/getenv "WEBSERVER_PORT") 3000)
                        :host (or (System/getenv "WEBSERVER_URL") "0.0.0.0")}}
          (map :system-config modules-config)))

(def webapp-dependencies
  (vec (mapcat :webapp-dependencies modules-config)))

(def routes
  (->> (filter :routes modules-config)
       (map :routes)
       (reduce #(set/union %1 %2))))

(defn general-components
  [config]
  {:service-map {:env         (:env config)
                 ::http/type  :jetty
                 ::http/port  (get-in config [:web-server :port])
                 ::http/host  (get-in config [:web-server :host])
                 ::http/join? false}
   :pedestal    (component/using (pedestal/new-pedestal routes) webapp-dependencies)})

(defn fill-system-map-keyval
  [system-map [key val]]
  (partial system-map key val))

(defn build-system-map
  [initial-system-map components]
  (reduce fill-system-map-keyval initial-system-map components))

(defn new-system
  [config]
  (let [system-map (build-system-map (partial component/system-map :config config)
                                     (concat (general-components config)
                                             (mapcat :system-components modules-config)))]
    (system-map)))

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (component/start (new-system system-config)))

(defn -stop
  "The entry-point for 'lein run'"
  [system]
  (component/stop system))
(when (= (:env system-config) :dev)
  (set-init (constantly (component/start (new-system system-config)))))

