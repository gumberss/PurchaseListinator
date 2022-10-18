(ns purchase-listinator.endpoints.http.shopping
  (:require [schema.core :as s]
            [cats.monad.either :refer :all]
            [purchase-listinator.misc.http :as misc.http]
            [purchase-listinator.misc.either :as misc.either]
            [purchase-listinator.flows.shopping :as flows.shopping]
            [purchase-listinator.adapters.out.shopping-initiation-data :as adapters.out.shopping-initiation-data]
            [purchase-listinator.adapters.in.shopping-initiation :as adapters.in.shopping-initiation]
            [purchase-listinator.adapters.in.shopping-initiation-data-request :as adapters.in.shopping-initiation-data-request]
            [purchase-listinator.adapters.misc :as adapters.misc]))

(s/defn init-shopping
  [{component :component
    wire      :json-params}]
  (misc.http/default-branch (misc.either/try-right
                              (-> (adapters.in.shopping-initiation/wire->internal wire)
                                  (flows.shopping/init-shopping component)))))
(s/defn get-init-shopping-data
  [{component    :component
    query-params :query-params}]
  (misc.http/default-branch-adapter (misc.either/try-right
                                      (-> (adapters.in.shopping-initiation-data-request/wire->internal query-params)
                                          (flows.shopping/get-initial-data component)))
                                    adapters.out.shopping-initiation-data/shopping->wire))

(s/defn existent-shopping
  [{component         :component
    {:keys [list-id]} :path-params}]
  (misc.http/default-branch (misc.either/try-right
                              (-> (adapters.misc/string->uuid list-id)
                                  (flows.shopping/find-existent component)))))

(s/defn get-shopping-list
  [{component             :component
    {:keys [id]} :path-params}]
  (misc.http/default-branch (misc.either/try-right
                              (-> (adapters.misc/string->uuid id)
                                  (flows.shopping/get-in-progress-list component)))))

(def routes
  #{["/api/shopping/init" :post [init-shopping] :route-name :post-init-shopping]
    ["/api/shopping/init" :get [get-init-shopping-data] :route-name :get-init-shopping-data]
    ["/api/shopping/existent/:list-id" :get [existent-shopping] :route-name :get-existent-shopping]
    ["/api/shopping/:id/list" :get [get-shopping-list] :route-name :get-in-progress]})
