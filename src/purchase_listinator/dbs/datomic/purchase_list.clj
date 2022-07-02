(ns purchase-listinator.dbs.datomic.purchase-list
  (:require [schema.core :as s]
            [datomic.api :as d]
            [purchase-listinator.adapters.db.purchase-list :as adapter.purchase-list]))

(def schema
  [{:db/ident       :purchase-list/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "The purchase-list id"}
   {:db/ident       :purchase-list/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "The purchase-list name"}
   {:db/ident       :purchase-list/enabled
    :db/valueType   :db.type/boolean
    :db/cardinality :db.cardinality/one
    :db/doc         "Disable if purchase-list is deleted"}
   {:db/ident       :purchase-list/in-progress
    :db/valueType   :db.type/boolean
    :db/cardinality :db.cardinality/one
    :db/doc         "If true, someone is buying the itens in this list"}
   {:db/ident       :purchase-list/products
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc         "Purchase-list Product Items"}])

(s/defn ^:private transact
  [connection & purchases-lists]
  @(d/transact connection purchases-lists))

(s/defn get-enabled
  [{:keys [connection]}]
  (->> (d/q '[:find [(pull ?e [*]) ...]
             :where
             [?e :purchase-list/enabled true]]
           (d/db connection))
      (map adapter.purchase-list/db->internal)))



(s/defn get-enabled-range
  [{:keys [connection]}]
  (->> (d/q '[:find [(pull ?e [*]) ...]
              :where
              [?e :purchase-list/enabled true]]
            (d/db connection))
       (map adapter.purchase-list/db->internal)))

(s/defn create
  [purchase-list {:keys [connection]}]
  (->> (adapter.purchase-list/internal->db purchase-list)
       (transact connection))
  purchase-list)

