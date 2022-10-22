(ns purchase-listinator.adapters.in.shopping-cart-event
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
            [purchase-listinator.wires.in.shopping-cart :as wires.in.shopping-cart]
            [purchase-listinator.adapters.misc :as adapters.misc]))

(s/defn wire->internal :- models.internal.shopping-cart/OrderCategoryEvent
  [{:keys [event-type shopping-id category-id] :as wire} :- wires.in.shopping-cart/OrderCategoryEvent
   moment :- s/Num]
  (assoc wire :event-type (keyword event-type)
              :shopping-id (adapters.misc/string->uuid shopping-id)
              :category-id (adapters.misc/string->uuid category-id)
              :moment moment))
