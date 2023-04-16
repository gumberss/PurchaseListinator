(ns purchase-listinator.modules.price-suggestion.flows.item-price-suggestion
  (:require
    [clojure.data.json :as json]
    [schema.core :as s]
    [clj-http.client :as client]))

(s/defn suggest-price
  [items-ids :- [s/Uuid]
   headers
   {{urls :price-suggestion/request-routes} :config}]
  #_(client/get (:shopping-events/get-events-by-items urls) {:headers      headers
                                                           :query-params (json/write-str items-ids)})
  :ok)

