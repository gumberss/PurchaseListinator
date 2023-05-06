(ns purchase-listinator.modules.price-suggestion.diplomat.http.server
  (:require
    [purchase-listinator.adapters.misc :as adapters.misc]
    [purchase-listinator.misc.either :as misc.either]
    [purchase-listinator.misc.http :as misc.http]
    [purchase-listinator.modules.price-suggestion.flows.item-price-suggestion :as flows.item-price-suggestion]
    [purchase-listinator.modules.price-suggestion.adapters.out.price-suggestion :as adapters.out.price-suggestion]
    [purchase-listinator.modules.price-suggestion.schemas.wire.out.price-suggestion :as schemas.wire.out.price-suggestion]
    [schema.core :as s]))

(s/defn get-price-by-item :- {:status s/Int
                              :body   s/Any}
  [{:keys              [component]
    {item-id :item-id} :path-params
    {:keys [headers]}  :request}]
  (misc.http/default-branch
    (misc.either/try-right
      :ok)))

(s/defn get-price-by-items :- {:status s/Int
                               :body   schemas.wire.out.price-suggestion/ShoppingItemSuggestedPrices}
  [{:keys               [component]
    {:keys [items-ids]} :params
    {:keys [user-id]}   :request}]
  (misc.http/default-branch
    (misc.either/try-right
      (-> (map adapters.misc/string->uuid items-ids)
          (flows.item-price-suggestion/suggest-price user-id component)
          (adapters.out.price-suggestion/internals->wire)))))


(def routes
  #{["/api/price-suggestion/by/item/:item-id" :get [get-price-by-item] :route-name :get-price-suggestion-by-item-id]
    ["/api/price-suggestion/by/items" :get [get-price-by-items] :route-name :get-price-suggestion-by-items-ids]})

