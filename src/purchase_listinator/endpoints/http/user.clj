(ns purchase-listinator.endpoints.http.user
  (:require [schema.core :as s]
            [cats.monad.either :refer :all]
            [purchase-listinator.misc.http :as misc.http]
            [purchase-listinator.misc.either :as misc.either]
            [purchase-listinator.flows.user :as flows.user]))


(s/defn register-user
  [{component :component
    :keys     [user-id]}]
  (misc.http/default-branch (misc.either/try-right
                              (flows.user/register user-id component))))


(def routes
  #{["/api/users/register" :post [register-user] :route-name :register-user]})
