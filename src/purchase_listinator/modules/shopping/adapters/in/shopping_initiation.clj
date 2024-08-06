(ns purchase-listinator.modules.shopping.adapters.in.shopping-initiation
  (:require [purchase-listinator.wires.in.shopping :as wires.in.shopping]
           [purchase-listinator.modules.shopping.schemas.models.shopping-initiation :as shopping.models.shopping-initiation]
           [purchase-listinator.modules.shopping.adapters.misc :as adapters.misc]
           [schema.core :as s]))

(s/defn wire->internal :- shopping.models.shopping-initiation/ShoppingInitiation
  [{:keys [id list-id] :as wire} :- wires.in.shopping/ShoppingInitiation]
  (assoc wire :id (adapters.misc/string->uuid id)
              :list-id (adapters.misc/string->uuid list-id)))
