(ns purchase-listinator.modules.shopping-cart.flows.cart
  (:require
    [purchase-listinator.logic.errors :as logic.errors]
    [purchase-listinator.misc.general :as misc.general]
    [purchase-listinator.modules.shopping-cart.schemas.internal.start-shopping :as schemas.internal.start-shopping]
    [purchase-listinator.modules.shopping-cart.schemas.wire.in.purchase-list :as modules.shopping-cart.schemas.wire.in.purchase-list]
    [schema.core :as s]
    [purchase-listinator.modules.shopping-cart.diplomat.http.client :as diplomat.http.client]
    [purchase-listinator.modules.shopping-cart.logic.purchase-list :as logic.purchase-list]
    [purchase-listinator.modules.shopping-cart.logic.price-suggestion :as logic.price-suggestion]
    [purchase-listinator.modules.shopping-cart.diplomat.db.redis :as diplomat.db.redis]))

(s/defn build-price-suggestion-events
  [{:keys [id] :as list} :- modules.shopping-cart.schemas.wire.in.purchase-list/PurchaseList
   user-id :- s/Uuid
   http]
  (-> (logic.purchase-list/find-items-ids list)
      (diplomat.http.client/get-price-suggestion user-id http)
      :price-suggestion
      (->> (map (partial logic.price-suggestion/->cart-event (misc.general/squuid) id user-id)))))

(s/defn start-cart
  [{:keys [list-id shopping-id]} :- schemas.internal.start-shopping/StartShopping
   user-id :- s/Uuid
   {:keys [shopping-cart/http shopping-cart/redis]}]
  (if-let [list (diplomat.db.redis/find-list list-id redis)]
    (do (diplomat.db.redis/add-shopping shopping-id list-id redis)
        {:purchase-list list
         :cart-events   (diplomat.db.redis/get-events list-id redis)})
    (if-let [list (diplomat.http.client/get-purchase-list list-id user-id http)]
      (do
        (diplomat.db.redis/add-list list redis)
        (diplomat.db.redis/add-shopping shopping-id list-id redis)
        (let [events (build-price-suggestion-events list user-id http)]
          {:purchase-list list
           :cart-events   (diplomat.db.redis/add-events list-id events redis)}))
      (logic.errors/build-left 404 "[[PURCHASE_LIST_NOT_FOUND]]"))))

