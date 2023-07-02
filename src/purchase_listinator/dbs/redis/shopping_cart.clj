(ns purchase-listinator.dbs.redis.shopping-cart
  (:require
    [schema.core :as s]
    [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
    [purchase-listinator.components.redis :as redis]))

(s/defn init-cart :- models.internal.shopping-cart/Cart
  [{:keys [shopping-id] :as cart} :- models.internal.shopping-cart/Cart
   redis :- redis/IRedis]
  (if-let [existent (redis/get-data redis shopping-id)]
    existent
    (redis/set-data redis shopping-id cart)))

(s/defn find-cart :- (s/maybe models.internal.shopping-cart/Cart)
  [id :- s/Uuid
   redis]
  (redis/get-data redis id))

(s/defn upsert :- models.internal.shopping-cart/Cart
  [{:keys [shopping-id] :as cart} :- models.internal.shopping-cart/Cart
   redis]
  (redis/set-data redis shopping-id cart)
  cart)

(s/defn delete :- models.internal.shopping-cart/Cart
  [shopping-id :- s/Uuid
   redis]
  (redis/del-data redis shopping-id))

