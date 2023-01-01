(ns purchase-listinator.wires.out.shopping-initiation-data
  (:require [schema.core :as s]))

(def shopping-initiation-data-skeleton
  {:place s/Str
   :type  s/Str})
(s/defschema ShoppingInitiationData shopping-initiation-data-skeleton)
