(ns purchase-listinator.flows.purchase-list
  (:require
    [purchase-listinator.logic.errors :as logic.errors]
    [purchase-listinator.models.internal.purchase-list.share :as models.internal.purchase-list.share]
    [schema.core :as s]
    [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
    [purchase-listinator.models.internal.purchase-list.purchase-list :as internal.purchase-list]
    [purchase-listinator.logic.purchase-list :as logic.purchase-list]
    [purchase-listinator.logic.purchase-category :as logic.purchase-category]
    [purchase-listinator.dbs.datomic.share :as dbs.datomic.share]
    [cats.monad.either :refer [left right]]
    [purchase-listinator.misc.either :as either]
    [purchase-listinator.dbs.datomic.user :as dbs.datomic.user]
    [purchase-listinator.logic.share :as logic.share]
    [purchase-listinator.models.internal.purchase-list.purchase-list-management-data :as internal.purchase-list-management-data]))

(s/defn get-lists
  [user-id :- s/Uuid
   datomic]
  (datomic.purchase-list/get-enabled user-id datomic))

(s/defn create
  [user-id :- s/Uuid
   name :- s/Str
   datomic]
  (either/try-right
    (if (datomic.purchase-list/get-by-name name user-id datomic)
      (left {:status 400
             :error  {:message "[[LIST_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
      (-> (logic.purchase-list/generate-new name user-id)
          (datomic.purchase-list/upsert datomic)))))

(s/defn disable
  [id :- s/Uuid
   user-id :- s/Uuid
   datomic]
  (if (datomic.purchase-list/existent? id user-id datomic)
    (datomic.purchase-list/disable id datomic)))

(s/defn edit
  [{:keys [id name] :as purchase-list} :- internal.purchase-list/PurchaseList
   user-id :- s/Uuid
   datomic]
  (either/try-right
    (let [existent-purchase-list (datomic.purchase-list/get-by-id id user-id datomic)]
      (cond
        (not existent-purchase-list) (left {:status 400
                                            :error  "[[PURCHASE_LIST_NOT_FOUND]]"})
        (not (logic.purchase-list/changed? existent-purchase-list purchase-list)) (right existent-purchase-list)
        (some? (datomic.purchase-list/get-by-name name user-id datomic)) (left {:status 400
                                                                                :error  {:message "[[LIST_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
        :else (-> (logic.purchase-list/edit existent-purchase-list purchase-list)
                  (datomic.purchase-list/upsert datomic)
                  right)))))

(s/defn share
  [{:keys [list-id customer-nickname] :as share-list-request} :- models.internal.purchase-list.share/ShareListRequest
   user-id :- s/Uuid
   datomic]
  (if (datomic.purchase-list/existent? list-id user-id datomic)
    (if-let [share-with-customer-id (dbs.datomic.user/get-id-by-nickname customer-nickname datomic)]
      (-> (logic.share/->share-list share-list-request share-with-customer-id)
          (dbs.datomic.share/upsert datomic))
      (logic.errors/build 404 {:message "[[USER_NOT_FOUND_BY_NICKNAME]]"}))
    (logic.errors/build 404 {:message "[[LIST_NOT_FOUND]]"})))

(s/defn management-data :- internal.purchase-list-management-data/ManagementData
  [purchase-list-id :- s/Uuid
   user-id :- s/Uuid
   datomic]
  (let [{:keys [categories] :as management-data} (datomic.purchase-list/get-management-data purchase-list-id user-id datomic)]
    (->> (map logic.purchase-category/sort-items-by-position categories)
         logic.purchase-category/sort-by-position
         (assoc management-data :categories))))
