(ns purchase-listinator.modules.shopping.schemas.wires.in.price-suggestion
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping.schemas.models.price-suggestion :as models.internal.price-suggestion]))

(s/def ShoppingItemSuggestedPrice
  models.internal.price-suggestion/ShoppingItemSuggestedPrice)

(s/def ShoppingItemSuggestedPrices
  models.internal.price-suggestion/ShoppingItemSuggestedPrices)
