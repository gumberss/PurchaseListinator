(ns purchase-listinator.modules.shopping.adapters.in.shopping-initiation-data-request
  (:require [purchase-listinator.modules.shopping.schemas.wires.in.shopping :as wires.in.shopping]
            [purchase-listinator.modules.shopping.schemas.models.shopping-initiation-data-request :as models.shopping-initiation-data-request]
            [purchase-listinator.adapters.misc :as adapters.misc]
            [schema.core :as s]))

(s/defn wire->internal :- models.shopping-initiation-data-request/ShoppingInitiationDataRequest
  [{:keys [list-id latitude longitude] :as wire} :- wires.in.shopping/ShoppingInitiationDataRequest]
  (assoc wire :list-id (adapters.misc/string->uuid list-id)
              :latitude (Double/parseDouble latitude)
              :longitude (Double/parseDouble longitude)))
