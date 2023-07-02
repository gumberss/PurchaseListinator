(ns purchase-listinator.components.redis
  (:require [com.stuartsierra.component :as component]))

(defrecord Redis [config-key config]
  component/Lifecycle
  (start [this]
    (let [{{:keys [username password host port timeout]} config-key} config
          conn {:pool {}
                :spec {:host       host
                       :port       port
                       :username   username
                       :password   password
                       :timeout-ms timeout}}]
      (assoc this
        :connection conn)))
  (stop [this]
    (dissoc this :connection)))

(defn new-Redis
  [config-key]
  (map->Redis config-key))

(defn new-redis-mock
  [config-key]
  (map->Redis config-key))


