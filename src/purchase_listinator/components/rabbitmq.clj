(ns purchase-listinator.components.rabbitmq
  (:require [langohr.core :as rmq]
            [langohr.channel :as lch]
            [langohr.queue :as lq]
            [langohr.consumers :as lc]
            [langohr.exchange :as le]
            [langohr.basic :as lb]
            [com.stuartsierra.component :as component]
            [purchase-listinator.endpoints.queue.shopping-purchase-list-event-received :as endpoints.queue.shopping-purchase-list-event-received]
            [purchase-listinator.misc.content-type-parser :as misc.content-type-parser]
            [cheshire.core :as cheshire]
            [schema.core :as s]
            [clojure.data.json :as json]
            [camel-snake-kebab.core :as csk]))

(def subscribers (concat
                   endpoints.queue.shopping-purchase-list-event-received/subscribers))

(s/defn ->rabbitmq :- s/Str
  [key :- s/Keyword]
  (subs (str key) 1))

(s/defn consumer-base
  [consumer
   channel
   metadata
   ^bytes payload]
  (let [data (String. payload "UTF-8")
        map-data (json/read-str data :key-fn csk/->kebab-case-keyword)
        map-data (json/read-str map-data :key-fn csk/->kebab-case-keyword)]
    (println "data")
    (println (type map-data))
    (println map-data)
    (consumer channel metadata map-data)))

(defn start-consumer
  [ch {:keys [exchange queue handler]}]
  (lq/declare ch (->rabbitmq queue) {:exclusive false :auto-delete false})
  (lq/bind ch (->rabbitmq queue) (->rabbitmq exchange))
  (lc/subscribe ch (->rabbitmq queue) (partial consumer-base handler) {:auto-ack true}))

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
               content-type)))

(defrecord RabbitMq [config]
  component/Lifecycle
  (start [this]
    (let [{rabbit-config :rabbitmq} config
          conn (rmq/connect rabbit-config)
          ch (lch/open conn)
          exchanges (map :exchange subscribers)]
      (doseq [e exchanges]
        (le/declare ch (->rabbitmq e) "fanout" {:durable true}))
      (doseq [s subscribers]
        (start-consumer ch s))
      (assoc this
        :connection conn
        :channel ch
        :publish publish)))
  (stop [this]
    (rmq/close (:channel this))
    (rmq/close (:connection this))
    (dissoc this :connection :channel :publish)))

(defn new-rabbit-mq
  []
  (map->RabbitMq {}))
