(ns purchase-listinator.adapters.in.purchase-item
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-item :as models.internal.purchase-item]
            [purchase-listinator.wires.in.purchase-item :as wires.in.purchase-item]
            [purchase-listinator.adapters.misc :as adapters.misc]))

(s/defn wire->internal :- models.internal.purchase-item/PurchaseItem
  [{:keys [id category-id] :as wire} :- wires.in.purchase-item/PurchaseItem]
  (assoc wire :id (adapters.misc/string->uuid id)
              :category-id (adapters.misc/string->uuid category-id)))
