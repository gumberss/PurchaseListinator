(ns purchase-listinator.dbs.datomic.shopping
  (:require [schema.core :as s]
            [datahike.api :as d]
            [purchase-listinator.adapters.db.shopping :as adapter.shopping]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]
            [purchase-listinator.adapters.db.shopping-item :as adapters.db.shopping-item]
            [purchase-listinator.adapters.db.shopping-category :as adapters.db.shopping-category]))

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
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping/user-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc         "user id"}])

(s/defn upsert
  [{:keys [categories] :as shopping} :- models.internal.shopping/Shopping
   {:keys [connection]}]
  (let [items (map adapters.db.shopping-item/internal->db (mapcat :items categories))
        categories (map adapters.db.shopping-category/internal->db categories)
        shopping (adapter.shopping/internal->db shopping)]
    (->> (concat [shopping] categories items)
         (apply misc.datomic/transact connection)))
  shopping)


(s/defn get-in-progress-by-list-id :- [models.internal.shopping/Shopping]
  [list-id :- s/Uuid
   allowed-lists-ids :- [s/Uuid]
   {:keys [connection]}]
  (->> (d/q '[:find [(pull ?s [* {:shopping/list [:purchase-list/id]}]) ...]
              :in $ ?l-id [?a-l-id ...]
              :where
              [?l :purchase-list/id ?l-id]
              [?l :purchase-list/id ?a-l-id]
              [?s :shopping/list ?l]
              [?s :shopping/status :in-progress]]
            (d/db connection) list-id allowed-lists-ids)
       (map adapter.shopping/db->internal)))

(s/defn get-by-id :- models.internal.shopping/Shopping
  [id :- s/Uuid
   allowed-lists-ids :- [s/Uuid]
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?s [* {:shopping/list [:purchase-list/id]}])
              :in $ ?id [?a-l-id ...]
              :where
              [?s :shopping/id ?id]
              [?s :shopping/list ?l]
              [?l :purchase-list/id ?a-l-id]]
            (d/db connection) id allowed-lists-ids)
       ffirst
       adapter.shopping/db->internal))
