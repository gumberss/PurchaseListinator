(ns purchase-listinator.modules.shopping-cart.schemas.wire.in.price-suggestion
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping-cart.schemas.internal.price-suggestion :as internal.price-suggestion]))

(s/def ShoppingItemSuggestedPrices
  internal.price-suggestion/ShoppingItemSuggestedPrices)

