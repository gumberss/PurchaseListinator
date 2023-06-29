(ns purchase-listinator.endpoints.http.client.shopping
  (:require
    [purchase-listinator.components.http :as components.http]
    [purchase-listinator.models.internal.price-suggestion :as models.internal.price-suggestion]
    [purchase-listinator.wires.in.price-suggestion :as wires.in.price-suggestion]
    [purchase-listinator.adapters.in.price-suggestion :as adapters.in.price-suggestion]
    [schema.core :as s]))

(s/defn get-price-suggestion :- models.internal.price-suggestion/ShoppingItemSuggestedPrices
  [items-ids :- [s/Uuid]
   user-id :- s/Uuid
   http]
  (-> (components.http/request http {:method        :get
                                     :url           :price-suggestion/items
                                     :query-params  {:items-ids items-ids}
                                     :user-id       user-id
                                     :result-schema wires.in.price-suggestion/ShoppingItemSuggestedPrices})
      (adapters.in.price-suggestion/wire->internal)))

(s/defn get-allowed-lists :- [s/Uuid]
  [user-id :- s/Uuid
   http]
  (components.http/request http {:method        :get
                                 :url           :purchase-list/allowed-lists
                                 :user-id       user-id
                                 :result-schema [s/Uuid]}))