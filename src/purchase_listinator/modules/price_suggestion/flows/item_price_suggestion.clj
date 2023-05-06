(ns purchase-listinator.modules.price-suggestion.flows.item-price-suggestion
  (:require
    [purchase-listinator.misc.date :as misc.date]
    [purchase-listinator.modules.price-suggestion.diplomat.http.client :as diplomat.http.client]
    [purchase-listinator.modules.price-suggestion.logic.price-suggestion :as logic.price-suggestion]
    [purchase-listinator.modules.price-suggestion.schemas.internal.price-suggestion :as internal.price-suggestion]
    [schema.core :as s]))

(s/defn suggest-price :- [internal.price-suggestion/ShoppingItemSuggestedPrice]
  [items-ids :- [s/Uuid]
   user-id :- s/Uuid
   {:price-suggestion/keys [http]}]
  (let [items-events (diplomat.http.client/get-items-events items-ids user-id http)
        predicted-date (misc.date/numb-now)
        price-suggestions (logic.price-suggestion/calculate-for-all items-events predicted-date)]
    price-suggestions))

