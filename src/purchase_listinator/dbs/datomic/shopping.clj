(ns purchase-listinator.dbs.datomic.shopping
  (:require [schema.core :as s]
            [datomic.api :as d]
            [purchase-listinator.adapters.db.shopping :as adapter.shopping]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]))

(def schema
  [{:db/ident       :shopping/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}
   {:db/ident       :shopping/place
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping/type
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping/title
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping/date
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping/list
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping/status
    :db/valueType   :db.type/keyword
    :db/cardinality :db.cardinality/one}])

(s/defn upsert
  [shopping :- models.internal.shopping/Shopping
   {:keys [connection]}]
  (->> (adapter.shopping/internal->db shopping)
       (misc.datomic/transact connection))
  shopping)
