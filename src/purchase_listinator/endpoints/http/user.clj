(ns purchase-listinator.endpoints.http.user
  (:require
    [purchase-listinator.adapters.misc :as adapters.misc]
    [schema.core :as s]
    [cats.monad.either :refer :all]
    [purchase-listinator.misc.http :as misc.http]
    [purchase-listinator.misc.either :as misc.either]
    [purchase-listinator.flows.user :as flows.user]))


(s/defn register-user
  [{component :component
    :keys     [user-id]}]
  (misc.http/default-branch
    (misc.either/try-right
      (flows.user/register user-id component))))

(s/defn nickname-user
  [{component          :component
    {:keys [nickname]} :json-params
    :keys              [user-id]}]
  (misc.http/default-branch
    (misc.either/try-right
      (flows.user/set-nickname nickname (adapters.misc/string->uuid user-id) component))))
(def routes
  #{["/api/users/register" :post [register-user] :route-name :register-user]
    ["/api/users/nickname" :post [nickname-user] :route-name :nickname-user]})
