(ns purchase-listinator.components.redis
  (:require [com.stuartsierra.component :as component]
            [taoensso.carmine :as car :refer (wcar)]))

(defrecord Redis [config]
  component/Lifecycle
  (start [this]
    (let [{{:keys [password host port timeout]} :redis} config
          conn {:pool {}
                    :spec {:host       host
                           :port       port
                           :password   password
                           :timeout-ms timeout}}]
      (assoc this
        :connection conn)))
  (stop [this]
    (dissoc this :wcar)))

(defn new-Redis
  []
  (map->Redis {}))



