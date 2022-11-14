(ns purchase-listinator.endpoints.queue.shopping-purchase-list-event-received)

(defn purchase-list-event-received
  [channel
   _
   components
   payload]
  (println payload)
  (println "components")
  (println (keys components)))

(def subscribers
  [{:exchange :purchase-listinator/purchase-list.updated
    :queue   :purchase-listinator/shopping-list.update
    :handler purchase-list-event-received}])
