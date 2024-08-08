(ns purchase-listinator.modules.shopping.adapters.out.shopping-initiation-data
  (:require [purchase-listinator.modules.shopping.schemas.models.shopping :as models.internal.shopping]
            [purchase-listinator.modules.shopping.schemas.wires.out.shopping-initiation-data :as wires.out.shopping-initiation-data]
            [schema.core :as s]))

(s/defn shopping->wire :- wires.out.shopping-initiation-data/ShoppingInitiationData
  [{:keys [place type]} :- models.internal.shopping/Shopping]
  {:place place
   :type  type})
