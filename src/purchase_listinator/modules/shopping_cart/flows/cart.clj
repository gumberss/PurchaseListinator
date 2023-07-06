(ns purchase-listinator.modules.shopping-cart.flows.cart
  (:require
    [purchase-listinator.logic.errors :as logic.errors]
    [purchase-listinator.modules.shopping-cart.schemas.internal.start-shopping :as schemas.internal.start-shopping]
    [schema.core :as s]
    [purchase-listinator.modules.shopping-cart.diplomat.http.client :as diplomat.http.client]
    [purchase-listinator.modules.shopping-cart.diplomat.db.redis :as diplomat.db.redis]))

(s/defn start-cart
  [{:keys [list-id shopping-id]} :- schemas.internal.start-shopping/StartShopping
   user-id
   {:keys [http shopping-cart/redis]}]
  (if-let [list (diplomat.db.redis/find-list list-id redis)]
    (do (diplomat.db.redis/add-shopping shopping-id list-id redis)
        {:purchase-list list
         :cart-events   (diplomat.db.redis/get-events list-id redis)})
    (if-let [list (diplomat.http.client/get-purchase-list list-id user-id http)]
      (do
        (diplomat.db.redis/add-list list redis)
        (diplomat.db.redis/add-shopping shopping-id list-id redis)
        {:purchase-list list
         :cart-events   []})
      (logic.errors/build-left 404 "[[PURCHASE_LIST_NOT_FOUND]]"))))