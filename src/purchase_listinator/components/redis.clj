(ns purchase-listinator.components.redis
  (:require [com.stuartsierra.component :as component])
  (:import (java.net Socket)
           (taoensso.carmine.connections NonPooledConnectionPool Connection)))

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


(defrecord RedisMock [config]
  component/Lifecycle
  (start [this]
    (let [conn {:pool (NonPooledConnectionPool.)
                :spec {:host     "host"
                       :port     "port"
                       :password "password"}}
          socket (Socket.)
          connection (Connection. socket "mock" 1 2)]
      (assoc this
        :connection conn)))
  (stop [this]
    (dissoc this :connection)))

(defn new-redis-mock
  [config-key]
  (map->RedisMock config-key))


