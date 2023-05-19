(ns purchase-listinator.modules.price-suggestion.adapters.out.price-suggestion
  (:require [schema.core :as s]
            [purchase-listinator.modules.price-suggestion.schemas.internal.price-suggestion :as schemas.internal.price-suggestion]
            [purchase-listinator.modules.price-suggestion.schemas.wire.out.price-suggestion :as schemas.wire.out.price-suggestion]))

(s/defn internals->wire :- schemas.wire.out.price-suggestion/ShoppingItemSuggestedPrices
  [internals :- [schemas.internal.price-suggestion/ShoppingItemSuggestedPrice]]
  {:price-suggestion internals})