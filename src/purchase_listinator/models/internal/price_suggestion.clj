(ns purchase-listinator.models.internal.price-suggestion
  (:require [schema.core :as s]))

(s/defschema ShoppingItemSuggestedPrice
  {:item-id         s/Uuid
   :predicted-date  s/Num
   :suggested-price s/Num})

(s/defschema ShoppingItemSuggestedPrices
  {:price-suggestion [ShoppingItemSuggestedPrice]})

