(ns purchase-listinator.dbs.datomic.share
  (:require
    [purchase-listinator.misc.datomic :as misc.datomic]
    [purchase-listinator.models.internal.purchase-list.share :as models.internal.purchase-list.share]
    [purchase-listinator.adapters.purchase-list.out.share :as adapters.purchase-list.out.share]
    [schema.core :as s]))

(def schema
  [{:db/ident       :purchase-list-share/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}
   {:db/ident       :purchase-list-share/list-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident       :purchase-list-share/customer-nickname
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}])

(s/defn upsert :- models.internal.purchase-list.share/ShareList
  [share :- models.internal.purchase-list.share/ShareList
   {:keys [connection]}]
  (->> (adapters.purchase-list.out.share/internal->db share)
       (misc.datomic/transact connection))
  share)
