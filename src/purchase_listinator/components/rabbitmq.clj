(ns purchase-listinator.components.rabbitmq
  (:require [langohr.core :as rmq]
            [langohr.channel :as lch]
            [langohr.queue :as lq]
            [langohr.consumers :as lc]
            [langohr.exchange :as le]
            [com.stuartsierra.component :as component]
            [purchase-listinator.endpoints.queue.purchase-list-shopping-event-received :as endpoints.queue.purchase-list-shopping-event-received]
            [schema.core :as s]
            [clojure.data.json :as json]
            [camel-snake-kebab.core :as csk]
            [schema.coerce :as coerce]
            [purchase-listinator.components.rabbitmq-channel :as components.rabbitmq-channel])
  (:import (clojure.lang PersistentQueue)))

(s/defn ->rabbitmq :- s/Str
  [key :- s/Keyword]
  (subs (str key) 1))

(s/defn consumer-base
  [components
   schema
   consumer
   channel
   metadata
   ^bytes payload]
  ;todo: use purchase-listinator.misc.content-type-parser to coerce the data
  (try (let [data (String. payload "UTF-8")
             map-data (json/read-str data :key-fn csk/->kebab-case-keyword)
             map-data (if (string? map-data)
                        (json/read-str map-data :key-fn csk/->kebab-case-keyword) ;I don't know why I need to do it the second time
                        map-data)
             coerce-function (if schema
                               (coerce/coercer schema coerce/json-coercion-matcher) identity)
             coerced-data (coerce-function map-data)]
         (consumer channel metadata components coerced-data))
       (catch Exception e
         (clojure.pprint/pprint e)
         (throw e))))

(defn start-consumer
  [ch
   components
   {:keys [exchange queue schema handler]}]
  (lq/declare ch (->rabbitmq queue) {:exclusive false :auto-delete false})
  (lq/bind ch (->rabbitmq queue) (->rabbitmq exchange))
  (lc/subscribe ch (->rabbitmq queue) (partial consumer-base components schema handler) {:auto-ack true}))

;[DEPRECATED]: This RabbitMq record shouldn't be used anymore,
; use RabbitMqExistentChannel with the rabbitmq-channel component instead
(defrecord RabbitMq [subscribers config-key config]
  component/Lifecycle
  (start [this]
    (let [{rabbit-config config-key} config
          conn (rmq/connect rabbit-config)
          ch (lch/open conn)
          exchanges (map :exchange subscribers)]
      (doseq [e exchanges]
        (le/declare ch (->rabbitmq e) "fanout" {:durable true}))
      (doseq [s subscribers]
        (start-consumer ch this s))
      (assoc this
        :connection conn
        :channel ch
        ;todo: this component shouldn't reference another component directly. use the RabbitMqExistentChannel instead
        :publish (partial components.rabbitmq-channel/publish ch))))
  (stop [this]
    (rmq/close (:channel this))
    (rmq/close (:connection this))
    (dissoc this :connection :channel :publish)))

(defn new-rabbit-mq
  []
  (map->RabbitMq {:subscribers (concat
                                 endpoints.queue.purchase-list-shopping-event-received/subscribers)
                  :config-key  :rabbitmq}))

(defrecord RabbitMqExistentChannel [subscribers config-key channel-component config]
  component/Lifecycle
  (start [this]
    (let [{rabbitmq-channel channel-component} this
          exchanges (map :exchange subscribers)
          {:keys [channel]} rabbitmq-channel]
      (doseq [e exchanges]
        (le/declare channel (->rabbitmq e) "fanout" {:durable true}))
      (doseq [s subscribers]
        (start-consumer channel this s))
      this))
  (stop [this]
    (rmq/close (:channel this))
    (rmq/close (:connection this))
    (dissoc this :connection :channel :publish)))

(defn new-rabbit-mq-v2
  [config-key channel-component subscribers]
  (map->RabbitMqExistentChannel {:subscribers       subscribers
                                 :config-key        config-key
                                 :channel-component channel-component}))

(defn new-queue [] (PersistentQueue/EMPTY))

(def exchanges-mock (atom {}))

(defn first-event [exchange]
  (first (exchange @exchanges-mock)))

(s/defn publish-mock
  ([exchange :- s/Keyword
    payload :- {s/Any s/Any}]
   (publish-mock exchange payload {:content-type "application/json"}))
  ([exchange :- s/Keyword
    payload :- {s/Any s/Any}
    _]
   (swap! exchanges-mock update-in [exchange] (fn [data] (conj (or data (new-queue)) payload)))
   payload))

(defrecord RabbitMqFake [config]
  component/Lifecycle
  (start [this]
    (assoc this :publish publish-mock))
  (stop [this]
    (dissoc this :publish)))

(defn new-rabbit-mq-fake
  []
  (map->RabbitMqFake {}))

