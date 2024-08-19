(ns purchase-listinator.modules.shopping-cart.flows.cart-events-reception
  (:require
    [purchase-listinator.modules.shopping-cart.diplomat.db.redis :as diplomat.db.redis]
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [schema.core :as s]))

(s/defn receive-cart-event-by-list
  [{:keys [purchase-list-id shopping-id] :as event} :- internal.cart-events/CartEvent
   {:keys [shopping-cart/redis]}]
  (try
    (let [list-id (or purchase-list-id (diplomat.db.redis/get-list-id-by-shopping shopping-id redis))]
      (when (not (nil? (diplomat.db.redis/find-list list-id redis)))
        (diplomat.db.redis/add-event list-id (assoc event :purchase-list-id list-id) redis)))
    (catch Exception e
      (println e)
      (throw e)))
  event)

(s/defn receive-cart-event-in-batch-by-list
  [events :- [internal.cart-events/CartEvent]
   components]
  (mapv #(receive-cart-event-by-list % components) events)
  events)
