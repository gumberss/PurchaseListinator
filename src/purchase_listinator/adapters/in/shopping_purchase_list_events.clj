(ns purchase-listinator.adapters.in.shopping-purchase-list-events
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
            [purchase-listinator.wires.in.purchase-category-events :as wires.in.purchase-category-events]))

(s/defn category-deleted-event->internal :- models.internal.shopping-cart/PurchaseListCategoryDeleted
  [wire :- wires.in.purchase-category-events/PurchaseCategoryDeletedEvent
   moment :- s/Num]
  (assoc wire
    :event-type :purchase-list-category-deleted
    :moment moment))
