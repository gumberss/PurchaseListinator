(ns purchase-listinator.wires.out.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.wires.in.purchase-list :as wire.in.purchase-list]))

(s/defschema PurchaseList wire.in.purchase-list/PurchaseList)

(s/defschema PurchaseLists wire.in.purchase-list/PurchaseLists)