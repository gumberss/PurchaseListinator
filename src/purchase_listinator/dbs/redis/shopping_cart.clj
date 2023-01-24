(ns purchase-listinator.dbs.redis.shopping-cart
  (:require [taoensso.carmine :as car :refer (wcar)]
            [schema.core :as s]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]))


(s/defn create-cart[conn]
  (wcar conn
        (car/set "shopping" "lala")))

(s/defn init-cart :- models.internal.shopping-cart/Cart
  [{:keys [shopping-id] :as cart} :- models.internal.shopping-cart/Cart
   {:keys [connection]}]
  (if-let [existent (wcar connection (car/get shopping-id))]
    existent
    (wcar connection
          (car/set shopping-id cart))))

(s/defn find-cart :- (s/maybe models.internal.shopping-cart/Cart)
  [id :- s/Uuid
   {:keys [connection]}]
  (clojure.pprint/pprint connection)
  (wcar connection (car/get id)))

(s/defn upsert :- models.internal.shopping-cart/Cart
  [{:keys [shopping-id] :as cart} :- models.internal.shopping-cart/Cart
   {:keys [connection]}]
  (wcar connection
        (car/set shopping-id cart))
  cart)

(s/defn delete :- models.internal.shopping-cart/Cart
  [shopping-id :- s/Uuid
   {:keys [connection]}]
  (wcar connection
        (car/del shopping-id)))

