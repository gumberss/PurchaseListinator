(ns purchase-listinator.endpoints.http.shopping
  (:require [schema.core :as s]
            [cats.monad.either :refer :all]
            [purchase-listinator.misc.http :as misc.http]
            [purchase-listinator.misc.either :as misc.either]
            [purchase-listinator.flows.shopping :as flows.shopping]
            [purchase-listinator.adapters.in.shopping-initiation :as adapters.in.shopping-initiation]
            [purchase-listinator.adapters.misc :as adapters.misc]))

(s/defn init-shopping
  [{component :component
    wire      :json-params}]
  (misc.http/default-branch (misc.either/try-right
                              (-> (adapters.in.shopping-initiation/wire->internal wire)
                                  (flows.shopping/init-shopping component)))))

(s/defn existent-shopping
  [{component :component
    {:keys [list-id]}      :path-params}]
  (misc.http/default-branch (misc.either/try-right
                              (-> (adapters.misc/string->uuid list-id)
                                  (flows.shopping/find-existent component)))))

(def routes
  #{["/api/shopping/init" :post [init-shopping] :route-name :post-init-shopping]
    ["/api/shopping/init" :get [init-shopping] :route-name :get-init-shopping]
    ["/api/shopping/existent/:list-id" :get [existent-shopping] :route-name :get-existent-shopping]})
