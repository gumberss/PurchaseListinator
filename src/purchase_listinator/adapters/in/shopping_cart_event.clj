(ns purchase-listinator.adapters.in.shopping-cart-event
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
            [purchase-listinator.wires.in.shopping-cart :as wires.in.shopping-cart]
            [purchase-listinator.adapters.misc :as adapters.misc]))

(defmulti wire->internal (fn [{:keys [event-type]} _] (keyword event-type)))

(s/defmethod wire->internal :reorder-category :- models.internal.shopping-cart/ReorderCategoryEvent
  [{:keys [event-type shopping-id category-id] :as wire} :- wires.in.shopping-cart/ReorderCategoryEvent
   moment :- s/Num]
  (assoc wire :event-type (keyword event-type)
              :shopping-id (adapters.misc/string->uuid shopping-id)
              :category-id (adapters.misc/string->uuid category-id)
              :moment moment))

(s/defmethod wire->internal :reorder-item :- models.internal.shopping-cart/ReorderItemEvent
  [{:keys [event-type shopping-id new-category-id item-id] :as wire} :- wires.in.shopping-cart/ReorderItemEvent
   moment :- s/Num]
  (assoc wire :event-type (keyword event-type)
              :shopping-id (adapters.misc/string->uuid shopping-id)
              :item-id (adapters.misc/string->uuid item-id)
              :new-category-id (adapters.misc/string->uuid new-category-id)
              :moment moment))
