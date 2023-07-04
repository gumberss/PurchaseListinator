(ns purchase-listinator.flows.purchase-list
  (:require
    [purchase-listinator.logic.errors :as logic.errors]
    [purchase-listinator.misc.date :as misc.date]
    [schema.core :as s]
    [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
    [purchase-listinator.dbs.datomic.share :as dbs.datomic.share]
    [purchase-listinator.models.internal.purchase-list.purchase-list :as internal.purchase-list]
    [purchase-listinator.logic.purchase-list :as logic.purchase-list]
    [purchase-listinator.logic.purchase-category :as logic.purchase-category]
    [cats.monad.either :refer [left left? right]]
    [purchase-listinator.misc.either :as either]
    [purchase-listinator.models.internal.purchase-list.purchase-list-management-data :as internal.purchase-list-management-data]))

(s/defn get-lists
  [user-id :- s/Uuid
   datomic]
  (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)]
    (datomic.purchase-list/get-enabled allowed-lists-ids datomic)))

(s/defn create
  [user-id :- s/Uuid
   name :- s/Str
   datomic]
  (either/try-right
    (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)]
      (if (datomic.purchase-list/get-by-name name allowed-lists-ids datomic)
        (left {:status 400
               :error  {:message "[[LIST_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
        (-> (logic.purchase-list/generate-new name user-id)
            (datomic.purchase-list/upsert datomic))))))


(s/defn disable
  [list-id :- s/Uuid
   user-id :- s/Uuid
   datomic]
  (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)]
    (cond
      (datomic.purchase-list/existent? list-id user-id datomic) (datomic.purchase-list/disable list-id datomic)
      (some #{list-id} allowed-lists-ids) (-> (dbs.datomic.share/find-share-id user-id list-id datomic)
                                              (dbs.datomic.share/remove datomic)
                                              (and {:id list-id}))
      :else (left (logic.errors/build 404 "[[LIST_NOT_FOUND]]")))))

(s/defn edit
  [{:keys [id name] :as purchase-list} :- internal.purchase-list/PurchaseList
   user-id :- s/Uuid
   datomic]
  (either/try-right
    (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)
          existent-purchase-list (datomic.purchase-list/get-by-id id allowed-lists-ids datomic)]
      (cond
        (not existent-purchase-list) (left {:status 400
                                            :error  "[[PURCHASE_LIST_NOT_FOUND]]"})
        (not (logic.purchase-list/changed? existent-purchase-list purchase-list)) (right existent-purchase-list)
        (some? (datomic.purchase-list/get-by-name name allowed-lists-ids datomic)) (left {:status 400
                                                                                          :error  {:message "[[LIST_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
        :else (-> (logic.purchase-list/edit existent-purchase-list purchase-list)
                  (datomic.purchase-list/upsert datomic)
                  right)))))

(s/defn allowed-lists-by-user :- [s/Uuid]
  [user-id :- s/Uuid
   datomic]
  (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic))


(s/defn management-data :- internal.purchase-list-management-data/ManagementData
  [purchase-list-id :- s/Uuid
   user-id :- s/Uuid
   datomic]
  (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)
        {:keys [categories] :as management-data} (datomic.purchase-list/get-management-data purchase-list-id allowed-lists-ids datomic)]
    (->> (map logic.purchase-category/sort-items-by-position categories)
         logic.purchase-category/sort-by-position
         (assoc management-data :categories))))

(s/defn get-list-simple
  [purchase-list-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [moment]} :- (s/maybe {(s/optional-key :moment) s/Num})
   datomic]
  (let [moment (or moment (misc.date/numb-now))
        allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)
        list (datomic.purchase-list/get-management-data purchase-list-id allowed-lists-ids moment datomic)]
    (or list (logic.errors/build-left 404 {:message "[[LIST_NOT_FOUND]]"}))))