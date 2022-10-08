(ns purchase-listinator.endpoints.http.shopping
  (:require [schema.core :as s]
            [cats.monad.either :refer :all]
            [purchase-listinator.misc.http :as misc.http]
            [purchase-listinator.misc.either :as misc.either]))

(s/defn init-shopping
  [{{:keys [datomic]} :component
    wire              :json-params}]
  (misc.http/default-branch (misc.either/try-right
                              {:status 200
                               :body "Hi! :)"})))

(def routes
  #{["/api/shopping/init" :post [init-shopping] :route-name :post-init-shopping]
    ["/api/shopping/init" :get [init-shopping] :route-name :get-init-shopping]
    ["/api/shopping/existent" :get [init-shopping] :route-name :get-existent-shopping]})
