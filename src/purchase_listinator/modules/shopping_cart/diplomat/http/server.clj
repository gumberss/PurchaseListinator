(ns purchase-listinator.modules.shopping-cart.diplomat.http.server
  (:require
    [purchase-listinator.misc.either :as misc.either]
    [purchase-listinator.misc.http :as misc.http]
    [schema.core :as s]))


(s/defn get-version :- {:status s/Int
                        :body   {:version s/Keyword}}
  [_request]
  {:status 200
   :body   {:version :v1}})

(def routes
  #{["/api/shopping-cart/version" :get [get-version] :route-name :get-shopping-cart-version]})

