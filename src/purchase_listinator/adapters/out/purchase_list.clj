(ns purchase-listinator.adapters.out.purchase-list
  (:require [purchase-listinator.models.internal.purchase-list.purchase-list :as models.internal.purchase-list]
            [purchase-listinator.wires.purchase-list.out.purchase-list :as wires.out.purchase-list]
            [schema.core :as s]))

(s/defn internal->wire :- wires.out.purchase-list/PurchaseList
  [{:keys [id user-id] :as purchase-list} :- models.internal.purchase-list/PurchaseList]
  (merge purchase-list
         {:id (str id)
          :user-id (str user-id)}))
