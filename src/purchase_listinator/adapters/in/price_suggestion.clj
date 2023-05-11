(ns purchase-listinator.adapters.in.price-suggestion
  (:require [schema.core :as s]
            [purchase-listinator.wires.in.price-suggestion :as wires.in.price-suggestion]
            [purchase-listinator.models.internal.price-suggestion :as models.internal.price-suggestion]))

(s/defn wire->internal :- models.internal.price-suggestion/ShoppingItemSuggestedPrices
  [wire :- wires.in.price-suggestion/ShoppingItemSuggestedPrices]
  wire)