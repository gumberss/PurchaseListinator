(ns purchase-listinator.modules.shopping-cart.diplomat.db.redis
  (:require
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [schema.core :as s]
    [purchase-listinator.modules.shopping-cart.schemas.internal.purchase-list :as internal.purchase-list]
    [purchase-listinator.components.redis :as redis]))

(s/defn find-list :- (s/maybe internal.purchase-list/PurchaseList)
  [list-id :- s/Uuid
   redis :- redis/IRedis]
  (redis/get-data redis (str list-id "_list")))

(s/defn add-list :- internal.purchase-list/PurchaseList
  [{:keys [id] :as list} :- internal.purchase-list/PurchaseList
   redis :- redis/IRedis]
  (redis/set-data redis (str id "_list") list)
  list)

(s/defn add-shopping
  [shopping-id :- s/Uuid
   list-id :- s/Uuid
   redis :- redis/IRedis]
  (redis/sadd redis (str list-id "_shopping_sessions") shopping-id))

;todo: schema
(s/defn get-events
  [list-id :- s/Uuid
   redis :- redis/IRedis]
  (redis/smembers redis (str list-id "_global_cart")))

(s/defn add-event
  [list-id :- s/Uuid
   event :- internal.cart-events/CartEvent
   redis :- redis/IRedis]
  (redis/sadd redis (str list-id "_global_cart") event))

(s/defn add-events
  [list-id :- s/Uuid
   events :- [internal.cart-events/CartEvent]
   redis :- redis/IRedis]
  (map #(redis/sadd redis (str list-id "_global_cart") %) events))
