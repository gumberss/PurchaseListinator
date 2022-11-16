(ns purchase-listinator.adapters.out.purchase-list-category-events
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.wires.out.purchase-list-caegory-events :as wires.out.purchase-list-caegory-events]))

(s/defn ->CategoryDeletedEvent :- wires.out.purchase-list-caegory-events/CategoryDeletedEvent
  [{:keys [id purchase-list-id]} :- models.internal.purchase-category/PurchaseCategory]
  {:category-id      id
   :purchase-list-id purchase-list-id})
