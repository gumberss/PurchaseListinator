(ns purchase-listinator.modules.shopping.diplomat.http.client
  (:require
    [purchase-listinator.components.http :as components.http]
    [purchase-listinator.modules.shopping.schemas.models.cart :as models.internal.cart]
    [purchase-listinator.modules.shopping.schemas.models.cart-events :as internal.cart-events]
    [purchase-listinator.modules.shopping.schemas.models.price-suggestion :as models.internal.price-suggestion]
    [purchase-listinator.modules.shopping.schemas.models.shopping-list :as models.internal.shopping-list]
    [purchase-listinator.modules.shopping.schemas.wires.in.llm :as shopping.schemas.wires.in.llm]
    [purchase-listinator.modules.shopping.schemas.wires.in.price-suggestion :as wires.in.price-suggestion]
    [purchase-listinator.modules.shopping.adapters.in.price-suggestion :as adapters.in.price-suggestion]
    [purchase-listinator.modules.shopping.schemas.wires.in.cart :as wires.in.cart]
    [purchase-listinator.modules.shopping.adapters.in.cart :as adapters.in.cart]
    [purchase-listinator.modules.shopping.adapters.out.llm :as shopping.adapters.out.llm]
    [purchase-listinator.modules.shopping.adapters.in.llm :as shopping.adapters.in.llm]
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
   shopping-id :- s/Uuid
   user-id :- s/Uuid
   http :- components.http/IHttp]
  (-> (components.http/request http {:method        :get
                                     :url           :shopping-cart/cart
                                     :user-id       user-id
                                     :query-params  {:list-id     purchase-list-id
                                                     :shopping-id shopping-id}
                                     :result-schema wires.in.cart/Cart})
      (adapters.in.cart/wire->internal)))

(s/defn get-shopping-exclusive-cart :- models.internal.cart/Cart
  [purchase-list-id :- s/Uuid
   shopping-id :- s/Uuid
   user-id :- s/Uuid
   http :- components.http/IHttp]
  (-> (components.http/request http {:method        :get
                                     :url           :shopping-cart/exclusive-cart
                                     :user-id       user-id
                                     :query-params  {:list-id     purchase-list-id
                                                     :shopping-id shopping-id}
                                     :result-schema wires.in.cart/Cart})
      (adapters.in.cart/wire->internal)))

(s/defn post-cart-events-in-batch :- [internal.cart-events/CartEvent]
  [shopping :- models.internal.shopping-list/ShoppingList
   items :- [models.internal.shopping-list/ShoppingItem]
   now :- s/Num
   user-id :- s/Uuid
   http :- components.http/IHttp]
  (components.http/request http {:method  :post
                                 :url     :shopping-cart/receive-events-in-batch
                                 :user-id user-id
                                 :body    (adapters.in.cart/items->change-item-event items shopping now)}))

(s/defn post-interaction :- (s/maybe [s/Uuid])
  [request-id :- s/Uuid
   shopping :- models.internal.shopping-list/ShoppingList
   image :- s/Str
   user-id :- s/Uuid
   http :- components.http/IHttp]
  (try
    (-> (components.http/request http {:method        :post
                                       :url           :llm-client/interactions
                                       :user-id       user-id
                                       :body          (shopping.adapters.out.llm/->wire-mark-shopping request-id shopping image)
                                       :result-schema shopping.schemas.wires.in.llm/InteractionResponse})
        (shopping.adapters.in.llm/wire->model))
    (catch Exception e
      (println e))))