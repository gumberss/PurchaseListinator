(ns purchase-listinator.flows.share
  (:require
    [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
    [purchase-listinator.dbs.datomic.share :as dbs.datomic.share]
    [purchase-listinator.dbs.datomic.user :as dbs.datomic.user]
    [purchase-listinator.logic.errors :as logic.errors]
    [purchase-listinator.logic.share :as logic.share]
    [purchase-listinator.models.internal.purchase-list.share :as models.internal.purchase-list.share]
    [schema.core :as s]))

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