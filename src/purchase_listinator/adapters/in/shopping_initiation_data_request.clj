(ns purchase-listinator.adapters.in.shopping-initiation-data-request
  (:require [purchase-listinator.wires.in.shopping :as wires.in.shopping]
            [purchase-listinator.models.internal.shopping-initiation-data-request :as models.internal.shopping-initiation]
            [purchase-listinator.adapters.misc :as adapters.misc]
            [schema.core :as s]))

(s/defn wire->internal :- models.internal.shopping-initiation/ShoppingInitiationDataRequest
  [{:keys [list-id latitude longitude] :as wire} :- wires.in.shopping/ShoppingInitiationDataRequest]
  (assoc wire :list-id (adapters.misc/string->uuid list-id)
              :latitude (Double/parseDouble latitude)
              :longitude (Double/parseDouble longitude)))
