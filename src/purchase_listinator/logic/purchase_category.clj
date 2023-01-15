(ns purchase-listinator.logic.purchase-category
  (:require [purchase-listinator.models.internal.purchase-list.purchase-list-management-data :as internal.purchase-list-management-data]
            [schema.core :as s]))

(s/defn sort-items-by-position :- internal.purchase-list-management-data/ManagementCategory
  [{:keys [items] :as category} :- internal.purchase-list-management-data/ManagementCategory]
  (->> (sort-by :order-position items)
       (assoc category :items)))

(s/defn sort-by-position :- [internal.purchase-list-management-data/ManagementCategory]
  [categories :- [internal.purchase-list-management-data/ManagementCategory]]
  (sort-by :order-position categories))
