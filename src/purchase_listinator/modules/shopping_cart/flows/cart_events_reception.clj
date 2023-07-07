(ns purchase-listinator.modules.shopping-cart.flows.cart-events-reception
  (:require
    [purchase-listinator.modules.shopping-cart.diplomat.db.redis :as diplomat.db.redis]
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [schema.core :as s]))

(s/defn receive-cart-event-by-list
  [{:keys [purchase-list-id] :as event} :- internal.cart-events/CartEvent
   {:keys [shopping-cart/redis]}]
  (try
    (when (not (nil? (diplomat.db.redis/find-list purchase-list-id redis)))
      (diplomat.db.redis/add-event purchase-list-id event redis))
    (catch Exception e
      (println e)
      (throw e)))
  event)

