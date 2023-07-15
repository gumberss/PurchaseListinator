(ns purchase-listinator.components.rabbitmq-channel
  (:require
    [langohr.basic :as lb]
    [langohr.channel :as lch]
    [langohr.core :as rmq]
    [com.stuartsierra.component :as component]
    [purchase-listinator.misc.content-type-parser :as misc.content-type-parser]
    [schema.core :as s]))

(s/defn ->rabbitmq :- s/Str
  [key :- s/Keyword]
  (subs (str key) 1))

(defprotocol IProducer
  (publish [channel exchange payload] [channel exchange payload args]))

(s/defn publish
  ([channel
    exchange :- s/Keyword
    payload :- {s/Any s/Any}]
   (publish channel exchange payload {:content-type "application/json"}))
  ([channel
    exchange :- s/Keyword
    payload :- {s/Any s/Any}
    {:keys [content-type]}]
   (lb/publish channel
               (->rabbitmq exchange)
               ""
               (misc.content-type-parser/transform-content-to payload content-type)
               content-type)
   payload))

(defrecord RabbitMqChannel [config-key config]
  component/Lifecycle
  (start [this]
    (let [{rabbit-config config-key} config
          conn (rmq/connect rabbit-config)
          ch (lch/open conn)]
      (assoc this :channel ch)))
  (stop [this]
    (rmq/close (:channel this))
    (rmq/close (:connection this))
    (dissoc this :connection :channel))
  IProducer
  (publish
    [channel exchange payload]
    (publish (:channel channel) exchange payload))
  (publish
    [channel exchange payload args]
    (publish (:channel channel) exchange payload args)))

(defn new-rabbit-mq-channel
  [config-key]
  (map->RabbitMqChannel {:config-key config-key}))