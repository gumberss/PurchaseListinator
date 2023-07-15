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
  (publish [producer exchange payload] [producer exchange payload args]))

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
    [producer exchange payload]
    (publish (:channel producer) exchange payload))
  (publish
    [producer exchange payload args]
    (publish (:channel producer) exchange payload args)))

(defn new-rabbit-mq-channel
  [config-key]
  (map->RabbitMqChannel {:config-key config-key}))