(ns purchase-listinator.logic.purchase-list
  (:require [schema.core :as s]))

(s/defn fill-default-creation-values
  [purchase-list]
  (assoc purchase-list
    ;:id (random-uuid)
    :enabled true
    :products []))
