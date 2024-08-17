(ns purchase-listinator.modules.shopping.diplomat.http.server
  (:require [schema.core :as s]
            [cats.monad.either :refer :all]
            [purchase-listinator.misc.http :as misc.http]
            [purchase-listinator.misc.either :as misc.either]
            [purchase-listinator.modules.shopping.flows.shopping :as flows.shopping]
            [purchase-listinator.modules.shopping.adapters.out.shopping-initiation-data :as adapters.out.shopping-initiation-data]
            [purchase-listinator.modules.shopping.adapters.in.shopping-initiation :as adapters.in.shopping-initiation]
            [purchase-listinator.modules.shopping.adapters.in.shopping-initiation-data-request :as adapters.in.shopping-initiation-data-request]
            [purchase-listinator.adapters.misc :as adapters.misc]))

(s/defn init-shopping
  [{component :component
    wire      :json-params
    user-id   :user-id}]
  (misc.http/default-branch (misc.either/try-right
                              (-> (adapters.in.shopping-initiation/wire->internal wire)
                                  (flows.shopping/init-shopping (adapters.misc/string->uuid user-id) component)))))
(s/defn get-init-shopping-data
  [{component    :component
    query-params :query-params
    user-id      :user-id}]
  (misc.http/default-branch-adapter (misc.either/try-right
                                      (-> (adapters.in.shopping-initiation-data-request/wire->internal query-params)
                                          (flows.shopping/get-initial-data (adapters.misc/string->uuid user-id) component)))
                                    adapters.out.shopping-initiation-data/shopping->wire))

(s/defn active-shopping
  [{component         :component
    {:keys [list-id]} :path-params
    user-id           :user-id}]
  (misc.http/default-branch
    (misc.either/try-right
      (flows.shopping/active-shopping (adapters.misc/string->uuid list-id)
                                      (adapters.misc/string->uuid user-id)
                                      component))))

(s/defn get-shopping-list
  [{component             :component
    {:keys [shopping-id]} :path-params
    user-id               :user-id}]
  (misc.http/default-branch
    (misc.either/try-right
      (-> (adapters.misc/string->uuid shopping-id)
          (flows.shopping/get-in-progress-list (adapters.misc/string->uuid user-id) component)))))

(s/defn finish-shopping
  [{component             :component
    {:keys [shopping-id]} :path-params
    user-id               :user-id}]
  (misc.http/default-branch
    (misc.either/try-right
      (-> (adapters.misc/string->uuid shopping-id)
          (flows.shopping/finish (adapters.misc/string->uuid user-id) component)))))

(s/defn mark-shopping-items
  [{component                     :component
    {:keys [shopping-id]}         :path-params
    {:keys [request-id] :as wire} :json-params
    user-id                       :user-id}]
  (misc.http/default-branch
    (misc.either/try-right
          (flows.shopping/mark-items (adapters.misc/string->uuid request-id)
                                     (adapters.misc/string->uuid shopping-id)
                                     (adapters.misc/string->uuid user-id)
                                     (:image wire) component))))

(def routes
  #{["/api/shopping/init" :post [init-shopping] :route-name :post-init-shopping]
    ["/api/shopping/init" :get [get-init-shopping-data] :route-name :get-init-shopping-data]
    ["/api/shopping/active/:list-id" :get [active-shopping] :route-name :get-active-shopping]
    ["/api/shopping/list/:shopping-id" :get [get-shopping-list] :route-name :get-in-progress]
    ["/api/shopping/finish/:shopping-id" :post [finish-shopping] :route-name :finish-shopping]
    ["/api/shopping/mark-items/:shopping-id" :post [mark-shopping-items] :route-name :mark-shopping-items]})
