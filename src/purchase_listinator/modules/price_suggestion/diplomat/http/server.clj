(ns purchase-listinator.modules.price-suggestion.diplomat.http.server
  (:require
    [purchase-listinator.misc.either :as misc.either]
    [purchase-listinator.misc.http :as misc.http]
    [schema.core :as s]))

(s/defn get-price-by-item :- {:status s/Int
                              :body   s/Any}
  [{:keys              [component]
    {item-id :item-id} :path-params}]
  (misc.http/default-branch
    (misc.either/try-right
      :ok)))

(s/defn get-price-by-items :- {:status s/Int
                               :body   s/Any}
  [{:keys               [component]
    {:keys [items-ids]} :params}]
  (misc.http/default-branch
    (misc.either/try-right
      :ok)))


(def routes
  #{["/api/price-suggestion/by/item/:item-id" :get [get-price-by-item] :route-name :get-price-suggestion-by-item-id]
    ["/api/price-suggestion/by/items" :get [get-price-by-items] :route-name :get-price-suggestion-by-items-ids]})

