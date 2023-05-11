(ns purchase-listinator.wires.in.price-suggestion
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.price-suggestion :as models.internal.price-suggestion]))

(s/def ShoppingItemSuggestedPrice
  models.internal.price-suggestion/ShoppingItemSuggestedPrice)

(s/def ShoppingItemSuggestedPrices
  models.internal.price-suggestion/ShoppingItemSuggestedPrices)
