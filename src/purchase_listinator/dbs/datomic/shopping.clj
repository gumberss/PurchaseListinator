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

(s/defn get-in-progress-by-list-id :- models.internal.shopping/Shopping
  [list-id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?s [* {:shopping/list [:purchase-list/id]}])
              :in $ ?l-id
              :where
              [?l :purchase-list/id ?l-id]
              [?s :shopping/list ?l]
              [?s :shopping/status :in-progress]]
            (d/db connection) list-id)
       ffirst
       adapter.shopping/db->internal))

(s/defn get-in-progress-by-category-id :- models.internal.shopping/Shopping
  [category-id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?s [* {:shopping/list [:purchase-list/id]}])
              :in $ ?c-id
              :where
              [?c :purchase-category/id ?c-id]
              [?c :purchase-category/purchase-list ?l]
              [?s :shopping/list ?l]
              [?s :shopping/status :in-progress]]
            (d/db connection) category-id)
       ffirst
       adapter.shopping/db->internal))

(s/defn get-by-id :- models.internal.shopping/Shopping
  [id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?s [* {:shopping/list [:purchase-list/id]}])
              :in $ ?id
              :where
              [?s :shopping/id ?id]]
            (d/db connection) id)
       ffirst
       adapter.shopping/db->internal))
