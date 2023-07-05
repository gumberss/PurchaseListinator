(ns purchase-listinator.modules.shopping-cart.diplomat.http.server
  (:require
    [purchase-listinator.adapters.misc :as adapters.misc]
    [purchase-listinator.misc.either :as misc.either]
    [purchase-listinator.misc.http :as misc.http]
    [purchase-listinator.modules.shopping-cart.flows.cart :as flows.cart]
    [purchase-listinator.modules.shopping-cart.adapters.in.start-shopping :as adapters.in.start-shopping]
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

(def routes
  #{["/api/shopping-cart/version" :get [get-version] :route-name :get-shopping-cart-version]
    ["/api/shopping-cart/initiate" :post [start-cart] :route-name :post-start-cart]})

