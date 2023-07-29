(ns purchase-listinator.endpoints.http.client.shopping
  (:require
    [purchase-listinator.components.http :as components.http]
    [purchase-listinator.models.internal.cart :as models.internal.cart]
    [purchase-listinator.models.internal.price-suggestion :as models.internal.price-suggestion]
    [purchase-listinator.wires.in.price-suggestion :as wires.in.price-suggestion]
    [purchase-listinator.adapters.in.price-suggestion :as adapters.in.price-suggestion]
    [purchase-listinator.wires.in.cart :as wires.in.cart]
    [purchase-listinator.adapters.in.cart :as adapters.in.cart]
    [schema.core :as s]))

(s/defn get-price-suggestion :- models.internal.price-suggestion/ShoppingItemSuggestedPrices
  [items-ids :- [s/Uuid]
   user-id :- s/Uuid
   http :- components.http/IHttp]
  (-> (components.http/request http {:method        :get
                                     :url           :price-suggestion/items
                                     :query-params  {:items-ids items-ids}
                                     :user-id       user-id
                                     :result-schema wires.in.price-suggestion/ShoppingItemSuggestedPrices})
      (adapters.in.price-suggestion/wire->internal)))

(s/defn get-allowed-lists :- [s/Uuid]
  [user-id :- s/Uuid
   http :- components.http/IHttp]
  (components.http/request http {:method        :get
                                 :url           :purchase-list/allowed-lists
                                 :user-id       user-id
                                 :result-schema [s/Uuid]}))

(s/defn init-shopping-cart :- [s/Uuid]
  [shopping-id :- s/Uuid
   purchase-list-id :- s/Uuid
   user-id :- s/Uuid
   http :- components.http/IHttp]
  (components.http/request http {:method  :post
                                 :url     :shopping-cart/init-cart
                                 :user-id user-id
                                 :body    {:shopping-id shopping-id
                                           :list-id     purchase-list-id}}))

(s/defn get-shopping-cart :- models.internal.cart/Cart
  [purchase-list-id :- s/Uuid
   user-id :- s/Uuid
   http :- components.http/IHttp]
  (-> (components.http/request http {:method        :get
                                     :url           :shopping-cart/cart
                                     :user-id       user-id
                                     :query-params  {:list-id purchase-list-id}
                                     :result-schema wires.in.cart/Cart})
      (adapters.in.cart/wire->internal)))