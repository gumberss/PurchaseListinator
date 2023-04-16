(ns purchase-listinator.modules.price-suggestion.flows.item-price-suggestion
  (:require
    [schema.core :as s]
    [clj-http.client :as client]))

(s/defn suggest-price
  [items-ids :- [s/Uuid]
   user-id
   {{urls :price-suggestion/request-routes} :config}]
  (client/get (:shopping-events/get-events-by-items urls) {:headers {:authorization (str user-id) }}))

