(ns purchase-listinator.logic.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]))

(s/defn fill-default-creation-values
  [purchase-list]
  (assoc purchase-list
    :id (misc.general/squuid)
    :enabled true
    :in-progress false
    :products []))
