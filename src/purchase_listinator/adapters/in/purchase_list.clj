(ns purchase-listinator.adapters.in.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list :as models.internal.purchase-list]
            [purchase-listinator.wires.in.purchase-list :as wires.in.purchase-list]
            [purchase-listinator.adapters.misc :as adapters.misc]))

(s/defn wire->internal :- models.internal.purchase-list/PurchaseList
  [{:keys [id] :as wire} :- wires.in.purchase-list/PurchaseList]
  (->> (adapters.misc/string->uuid id)
       (assoc wire :id)))
