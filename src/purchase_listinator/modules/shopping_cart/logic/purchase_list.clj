(ns purchase-listinator.modules.shopping-cart.logic.purchase-list
  (:require
    [purchase-listinator.modules.shopping-cart.schemas.wire.in.purchase-list :as modules.shopping-cart.schemas.wire.in.purchase-list]
    [schema.core :as s]))

(s/defn find-items-ids :- [s/Uuid]
  [list :- modules.shopping-cart.schemas.wire.in.purchase-list/PurchaseList]
  (->> list
       :categories
       (mapcat :items)
       (map :id)))