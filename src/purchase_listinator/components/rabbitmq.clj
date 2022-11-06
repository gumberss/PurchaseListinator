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
            [schema.core :as s]))

(def subscribers (concat
                   endpoints.queue.shopping-purchase-list-event-received/subscribers))

(defn start-consumer
  [ch {:keys [exchange queue handler]}]
  (lq/declare ch (str queue) {:exclusive false :auto-delete true})
  (lq/bind ch (str queue) (str exchange))
  (lc/subscribe ch (str queue) handler {:auto-ack true}))

(s/defn publish
  ([channel
    exchange :- s/Keyword
    payload :- {s/Any s/Any}]
   (publish channel exchange payload))
  ([channel
    exchange :- s/Keyword
    payload :- {s/Any s/Any}
    {:keys [content-type]}]
   (lb/publish channel
               exchange
               ""
               (misc.content-type-parser/transform-response payload content-type)
               content-type)))

(defrecord RabbitMq [config]
  component/Lifecycle
  (start [this]
    (let [conn (rmq/connect)
          ch (lch/open conn)
          exchanges (map :exchange subscribers)]
      (doseq [e exchanges]
        (le/declare ch e "fanout" {:durable true}))
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
