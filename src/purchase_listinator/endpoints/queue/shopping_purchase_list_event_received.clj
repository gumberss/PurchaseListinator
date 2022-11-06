(ns purchase-listinator.endpoints.queue.shopping-purchase-list-event-received)

(defn purchase-list-event-received
  [channel
   _
   payload])

(def subscribers
  [ {:exhange :purchase-listinator/purchase-list.updated
     :queue :purchase-listinator/shopping-list.update
     :handler purchase-list-event-received}])
