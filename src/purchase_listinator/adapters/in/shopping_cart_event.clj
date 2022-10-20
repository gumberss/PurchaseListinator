(ns purchase-listinator.adapters.in.shopping-cart-event
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]))

(s/defn wire->internal :- models.internal.shopping-cart/CartEvent
  [wire]
  )
