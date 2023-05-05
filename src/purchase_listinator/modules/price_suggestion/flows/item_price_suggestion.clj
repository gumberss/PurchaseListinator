(ns purchase-listinator.modules.price-suggestion.flows.item-price-suggestion
  (:require
    [purchase-listinator.modules.price-suggestion.diplomat.http.client :as diplomat.http.client]
    [schema.core :as s]))

(s/defn suggest-price
  [items-ids :- [s/Uuid]
   user-id :- s/Uuid
   {:price-suggestion/keys [http]}]
  (diplomat.http.client/get-items-events items-ids user-id http)
  :ok)

