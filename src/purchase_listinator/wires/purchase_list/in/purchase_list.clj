(ns purchase-listinator.wires.purchase-list.in.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list :as models.internal.purchase-list]))

(def purchase-list-skeleton
  {:name        s/Str
   :id          s/Str
   :enabled     s/Bool
   :in-progress s/Bool})

(s/defschema PurchaseList purchase-list-skeleton)

(s/defschema PurchaseLists [PurchaseList])
