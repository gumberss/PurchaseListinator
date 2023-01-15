(ns purchase-listinator.adapters.out.purchase-list
  (:require [purchase-listinator.models.internal.purchase-list.purchase-list :as models.internal.purchase-list]
            [purchase-listinator.wires.purchase-list.out.purchase-list :as wires.out.purchase-list]
            [schema.core :as s]))

(s/defn internal->wire :- wires.out.purchase-list/PurchaseList
  [{:keys [id] :as wire} :- models.internal.purchase-list/PurchaseList]

  (assoc wire :id (str id)))
