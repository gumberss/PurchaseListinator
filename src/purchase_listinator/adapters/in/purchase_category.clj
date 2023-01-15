(ns purchase-listinator.adapters.in.purchase-category
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.wires.purchase-list.in.purchase-category :as wires.in.purchase-category]
            [purchase-listinator.adapters.misc :as adapters.misc]))

(s/defn wire->internal :- models.internal.purchase-category/PurchaseCategory
  [{:keys [id purchase-list-id] :as wire} :- wires.in.purchase-category/PurchaseCategory]
  (assoc wire :id (adapters.misc/string->uuid id)
              :purchase-list-id (adapters.misc/string->uuid purchase-list-id)))
