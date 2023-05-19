(ns purchase-listinator.wires.purchase-list.out.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.wires.purchase-list.in.purchase-list :as wire.in.purchase-list]))

(s/defschema PurchaseList
  (assoc wire.in.purchase-list/PurchaseList
    :id s/Uuid))

(s/defschema PurchaseLists wire.in.purchase-list/PurchaseLists)
