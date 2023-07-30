(ns purchase-listinator.modules.shopping-cart.flows.cart
  (:require
    [purchase-listinator.logic.errors :as logic.errors]
    [purchase-listinator.modules.shopping-cart.schemas.internal.purchase-list :as internal.purchase-list]
    [purchase-listinator.modules.shopping-cart.schemas.internal.shopping :as schemas.internal.shopping]
    [purchase-listinator.modules.shopping-cart.schemas.wire.in.purchase-list :as modules.shopping-cart.schemas.wire.in.purchase-list]
    [schema.core :as s]
    [purchase-listinator.modules.shopping-cart.logic.price-suggestion :as logic.price-suggestion]
    [purchase-listinator.modules.shopping-cart.diplomat.http.client :as diplomat.http.client]
    [purchase-listinator.modules.shopping-cart.logic.purchase-list :as logic.purchase-list]
    [purchase-listinator.modules.shopping-cart.diplomat.db.redis :as diplomat.db.redis]
    [purchase-listinator.modules.shopping-cart.diplomat.producers.shopping-cart-event :as producers.shopping-cart-event]
    [purchase-listinator.modules.shopping-cart.logic.cart :as logic.cart]
    [purchase-listinator.modules.shopping-cart.logic.events :as logic.events]
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart :as internal.cart]))

(s/defn ^:private build-price-suggestion-events
  [{:keys [id] :as list} :- modules.shopping-cart.schemas.wire.in.purchase-list/PurchaseList
   user-id :- s/Uuid
   http]
  (let [list-items (logic.purchase-list/find-items-ids list)]
    (when (not-empty list-items)
      (let [price-suggestions (-> (diplomat.http.client/get-price-suggestion list-items user-id http)
                                  :price-suggestion)]
        (map (partial logic.price-suggestion/generate-cart-event price-suggestions id user-id) list-items)))))

(s/defn start-cart
  [{:keys [list-id shopping-id]} :- schemas.internal.shopping/StartShopping
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
          (diplomat.db.redis/add-events list-id events redis)
          {:purchase-list list
           :cart-events   events}))
      (logic.errors/build-left 404 "[[PURCHASE_LIST_NOT_FOUND]]"))))

(s/defn close-cart
  [{{:keys [list-id id] :as shopping} :shopping} :- schemas.internal.shopping/CloseShopping
   {:keys [shopping-cart/redis shopping-cart/rabbitmq-channel]}]
  (when (diplomat.db.redis/find-list list-id redis)
    (diplomat.db.redis/remove-shopping id list-id redis)
    (let [all-events (diplomat.db.redis/get-events list-id redis)
          current-shopping-events (logic.events/filter-by-shopping id all-events)
          list-sessions (diplomat.db.redis/all-sessions list-id redis)]
      (when (empty? list-sessions)
        (diplomat.db.redis/delete-list-and-related list-id redis))
      (producers.shopping-cart-event/shopping-cart-closed shopping current-shopping-events rabbitmq-channel))))

(s/defn remove-list-cart
  [{:keys [list-id]} :- internal.purchase-list/PurchaseListDisabled
   {:keys [shopping-cart/redis]}]
  (when (diplomat.db.redis/find-list list-id redis)
    (diplomat.db.redis/delete-list-and-related list-id redis)))

(s/defn get-cart :- internal.cart/Cart
  [list-id :- s/Uuid
   {:keys [shopping-cart/redis]}]
  (let [list (diplomat.db.redis/find-list list-id redis)
        events (diplomat.db.redis/get-events list-id redis)]
    (if list
      (logic.cart/->cart list events)
      (logic.errors/build 404 "[[PURCHASE_LIST_NOT_FOUND]]"))))