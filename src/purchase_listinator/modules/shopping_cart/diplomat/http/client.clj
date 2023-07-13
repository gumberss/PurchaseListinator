(ns purchase-listinator.modules.shopping-cart.diplomat.http.client
  (:require
    [purchase-listinator.components.http :as components.http]
    [purchase-listinator.modules.shopping-cart.schemas.wire.in.purchase-list :as modules.shopping-cart.schemas.wire.in.purchase-list]
    [purchase-listinator.modules.shopping-cart.adapters.in.purchase-list :as modules.shopping-cart.adapters.in.purchase-list]
    [purchase-listinator.modules.shopping-cart.schemas.wire.in.price-suggestion :as wire.in.price-suggestion]
    [purchase-listinator.modules.shopping-cart.schemas.internal.price-suggestion :as internal.price-suggestion]
    [schema.core :as s]))

(s/defn get-purchase-list :- (s/maybe modules.shopping-cart.schemas.wire.in.purchase-list/PurchaseList)
  [list-id :- s/Uuid
   user-id :- s/Uuid
   http]
  (try (-> (components.http/request http {:method        :get
                                          :url           :purchase-list/purchase-list-by-id-simple
                                          :query-params  {:id list-id}
                                          :user-id       user-id
                                          :result-schema modules.shopping-cart.schemas.wire.in.purchase-list/PurchaseList})
           (modules.shopping-cart.adapters.in.purchase-list/purchase-list-wire->internal))
       (catch Exception ex
         (when (not= 404 (:status (ex-data ex)))
           (println "get-purchase-list")
           (println ex))
         nil)))

(s/defn get-price-suggestion :- internal.price-suggestion/ShoppingItemSuggestedPrices
  [items-ids :- [s/Uuid]
   user-id :- s/Uuid
   http]
  (components.http/request http {:method        :get
                                 :url           :price-suggestion/items
                                 :query-params  {:items-ids items-ids}
                                 :user-id       user-id
                                 :result-schema wire.in.price-suggestion/ShoppingItemSuggestedPrices}))