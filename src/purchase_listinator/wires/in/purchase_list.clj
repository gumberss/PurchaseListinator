(ns purchase-listinator.wires.in.purchase-list
  (:require [schema.core :as s]))

(def purchase-list-skeleton
  {:name   s/Str
   :status (s/enum [:in-progress :waiting])})

(s/defschema PurchaseList purchase-list-skeleton)

(s/defschema PurchaseLists [PurchaseList])