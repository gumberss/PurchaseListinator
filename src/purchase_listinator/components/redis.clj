(ns purchase-listinator.components.redis
  (:require [com.stuartsierra.component :as component])
  (:import (java.net Socket)
           (taoensso.carmine.connections NonPooledConnectionPool Connection)))

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
    (dissoc this :connection)))

(defn new-Redis
  []
  (map->Redis {}))


(defrecord RedisMock [config]
  component/Lifecycle
  (start [this]
    (clojure.pprint/pprint config)

    (let [conn {:pool (NonPooledConnectionPool.)
           :spec {:host       "host"
                  :port       "port"
                  :password   "password"}}
          socket (Socket.)
          connection (Connection. socket "mock" 1 2)]
      (assoc this
        :connection conn)))
  (stop [this]
    (dissoc this :connection)))

(defn new-redis-mock
  []
  (map->Redis {}))


