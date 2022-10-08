(ns purchase-listinator.adapters.in.shopping-initiation
  (:require [purchase-listinator.wires.in.shopping :as wires.in.shopping]
           [purchase-listinator.models.internal.shopping-initiation :as models.internal.shopping-initiation]
           [purchase-listinator.adapters.misc :as adapters.misc]
           [schema.core :as s]))

(s/defn wire->internal :- models.internal.shopping-initiation/ShoppingInitiation
  [{:keys [id list-id] :as wire} :- wires.in.shopping/ShoppingInitiation]
  (assoc wire :id (adapters.misc/string->uuid id)
              :list-id (adapters.misc/string->uuid list-id)))
