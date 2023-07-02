(ns purchase-listinator.components.redis_v2
  (:require [com.stuartsierra.component :as component]
            [taoensso.carmine :as car :refer (wcar)]))

(defprotocol IRedis
  (set-data [redis key val])
  (get-data [redis key])
  (del-data [redis key]))

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
    (dissoc this :connection))

  IRedis
  (set-data [{:keys [connection]}
        key
        val]
    (wcar connection
          (car/set key val)))
  (get-data [{:keys [connection]}
        key]
    (wcar connection
          (car/get key)))
  (del-data [{:keys [connection]}
        key]
    (wcar connection
          (car/del key))))

(defn new-Redis
  [config-key]
  (map->Redis config-key))

(defn new-redis-mock
  [config-key]
  (map->Redis config-key))


