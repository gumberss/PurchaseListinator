(ns purchase-listinator.modules.shopping-cart.diplomat.http.client
  (:require
    [purchase-listinator.components.http :as components.http]
    [purchase-listinator.modules.price-suggestion.schemas.internal.shopping-item-event :as internal.shopping-item-event]
    [purchase-listinator.modules.shopping-cart.schemas.wire.purchase-list :as modules.shopping-cart.schemas.wire.purchase-list]
    [schema.core :as s]))


(s/defn get-items-events :- internal.shopping-item-event/ShoppingItemEvents
  [list-id :- [s/Uuid]
   user-id :- s/Uuid
   http]
  (-> (components.http/request http {:method        :get
                                     :url           :purchase-list/purchase-list-by-id
                                     :query-params  {:id list-id}
                                     :user-id       user-id
                                     :result-schema modules.shopping-cart.schemas.wire.purchase-list/PurchaseList})
      (adapters.in.shopping-item-event/wire->internal)))