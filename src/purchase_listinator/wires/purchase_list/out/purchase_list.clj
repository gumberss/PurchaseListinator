(ns purchase-listinator.wires.purchase-list.out.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.wires.purchase-list.in.purchase-list :as wire.in.purchase-list]))

(s/defschema PurchaseList wire.in.purchase-list/purchase-list-skeleton)

(s/defschema PurchaseLists wire.in.purchase-list/PurchaseLists)
