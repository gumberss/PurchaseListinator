(ns purchase-listinator.logic.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.models.internal.purchase-list :as internal.purchase-list]))

(s/defn generate-new :- internal.purchase-list/PurchaseList
  [name :- s/Str]
  (let [purchase-list {:id          (misc.general/squuid)
                       :enabled     true
                       :in-progress false
                       :products    []}]
    (assoc purchase-list :name name)))
