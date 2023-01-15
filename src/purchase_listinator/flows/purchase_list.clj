(ns purchase-listinator.flows.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
            [purchase-listinator.models.internal.purchase-list.purchase-list :as internal.purchase-list]
            [purchase-listinator.logic.purchase-list :as logic.purchase-list]
            [purchase-listinator.logic.purchase-category :as logic.purchase-category]
            [cats.monad.either :refer [left right]]
            [purchase-listinator.misc.either :as either]
            [purchase-listinator.models.internal.purchase-list.purchase-list-management-data :as internal.purchase-list-management-data]))

(s/defn get-lists
  [datomic]
  (datomic.purchase-list/get-enabled datomic))

(s/defn create
  [{:keys [name]}
   datomic]
  (either/try-right
    (if (datomic.purchase-list/get-by-name name datomic)
      (left {:status 400
             :error  {:message "[[LIST_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
      (-> (logic.purchase-list/generate-new name)
          (datomic.purchase-list/upsert datomic)))))

(s/defn disable
  [id datomic]
  (either/try-right (datomic.purchase-list/disable id datomic)))

(s/defn edit
  [{:keys [id name] :as purchase-list} :- internal.purchase-list/PurchaseList
   datomic]
  (either/try-right
    (let [existent-purchase-list (datomic.purchase-list/get-by-id id datomic)]
      (cond
        (not existent-purchase-list) (left {:status 400
                                            :error  "[[PURCHASE_LIST_NOT_FOUND]]"})
        (not (logic.purchase-list/changed? existent-purchase-list purchase-list)) (right existent-purchase-list)
        (some? (datomic.purchase-list/get-by-name name datomic)) (left {:status 400
                                                                        :error  {:message "[[LIST_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
        :else (-> (logic.purchase-list/edit existent-purchase-list purchase-list)
                  (datomic.purchase-list/upsert datomic)
                  right)))))

(s/defn management-data :- internal.purchase-list-management-data/ManagementData
  [purchase-list-id :- s/Uuid
   datomic]
  (let [{:keys [categories] :as management-data} (datomic.purchase-list/get-management-data purchase-list-id datomic)]
    (->> (map logic.purchase-category/sort-items-by-position categories)
         logic.purchase-category/sort-by-position
         (assoc management-data :categories))))
