(ns purchase-listinator.dbs.redis.shopping
  (:require [taoensso.carmine :as car :refer (wcar)]
            [schema.core :as s]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]))

(s/defn upsert :- models.internal.shopping-cart/Cart
  [{:keys [id] :as cart} :- models.internal.shopping-cart/Cart
   {:keys [connection]}]
  (wcar connection
        (car/set id cart))
  cart)

