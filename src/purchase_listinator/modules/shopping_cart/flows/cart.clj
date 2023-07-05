(ns purchase-listinator.modules.shopping-cart.flows.cart
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping-cart.diplomat.http.client :as diplomat.http.client]))

(s/defn start-cart
  [{:keys [list-id]}
   user-id
   {:keys [http]}]
  (diplomat.http.client/get-items-events list-id user-id http))