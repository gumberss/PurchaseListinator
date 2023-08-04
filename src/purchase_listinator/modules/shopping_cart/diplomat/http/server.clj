(ns purchase-listinator.modules.shopping-cart.diplomat.http.server
  (:require
    [purchase-listinator.adapters.misc :as adapters.misc]
    [purchase-listinator.misc.date :as misc.date]
    [purchase-listinator.misc.either :as misc.either]
    [purchase-listinator.misc.http :as misc.http]
    [purchase-listinator.modules.shopping-cart.flows.cart :as flows.cart]
    [purchase-listinator.modules.shopping-cart.adapters.in.start-shopping :as adapters.in.start-shopping]
    [purchase-listinator.modules.shopping-cart.adapters.in.cart-events :as adapters.in.cart-events]
    [purchase-listinator.modules.shopping-cart.flows.cart-events-reception :as flows.cart-events-reception]
    [purchase-listinator.modules.shopping-cart.adapters.out.cart :as adapters.out.cart]
    [schema.core :as s]))


(s/defn get-version :- {:status s/Int :body {:version s/Keyword}}
  [_request]
  {:status 200
   :body   {:version :v1}})

(s/defn start-cart
  [{:keys [json-params user-id component]}]
  (misc.http/default-branch
    (misc.either/try-right
      (flows.cart/start-cart (adapters.in.start-shopping/wire->internal json-params)
                             (adapters.misc/string->uuid user-id)
                             component))))

(s/defn get-cart
  [{:keys                         [component]
    {:keys [list-id shopping-id]} :path-params}]
  (misc.http/default-branch-adapter
    (misc.either/try-right
      (flows.cart/get-cart (adapters.misc/string->uuid list-id)
                           (adapters.misc/string->uuid shopping-id)
                           component))
    adapters.out.cart/internal->wire))

(s/defn get-exclusive-cart
  [{:keys                         [component]
    {:keys [list-id shopping-id]} :path-params}]
  (misc.http/default-branch-adapter
    (misc.either/try-right
      (flows.cart/get-exclusive-cart (adapters.misc/string->uuid list-id)
                                     (adapters.misc/string->uuid shopping-id)
                                     component))
    adapters.out.cart/internal->wire))

(s/defn receive-events
  [{component :component
    wire      :json-params
    user-id   :user-id}]
  (misc.http/default-branch
    (misc.either/try-right
      (let [now (misc.date/numb-now)
            user-id (adapters.misc/string->uuid user-id)
            cart-event (adapters.in.cart-events/wire->internal wire now user-id)]
        (flows.cart-events-reception/receive-cart-event-by-list cart-event component)))))

(def routes
  #{["/api/shopping-cart/version" :get [get-version] :route-name :get-shopping-cart-version]
    ["/api/shopping-cart/initiate" :post [start-cart] :route-name :post-start-cart]
    ["/api/shopping-cart/events" :post [receive-events] :route-name :receive-cart-events]
    ["/api/shopping-cart/by/:list-id/:shopping-id" :get [get-cart] :route-name :get-cart]
    ["/api/shopping-cart/exclusive-by/:list-id/:shopping-id" :get [get-exclusive-cart] :route-name :get-exclusive-cart]})

