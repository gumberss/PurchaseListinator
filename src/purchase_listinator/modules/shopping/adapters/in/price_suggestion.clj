(ns purchase-listinator.modules.shopping.adapters.in.price-suggestion
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping.schemas.wires.in.price-suggestion :as wires.in.price-suggestion]
            [purchase-listinator.modules.shopping.schemas.models.price-suggestion :as models.internal.price-suggestion]))

(s/defn wire->internal :- models.internal.price-suggestion/ShoppingItemSuggestedPrices
  [wire :- wires.in.price-suggestion/ShoppingItemSuggestedPrices]
  wire)