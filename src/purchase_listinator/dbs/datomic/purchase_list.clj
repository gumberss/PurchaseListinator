(ns purchase-listinator.dbs.datomic.purchase-list
  (:require [schema.core :as s]
            [datahike.api :as d]
            [purchase-listinator.adapters.db.purchase-list :as adapter.purchase-list]
            [purchase-listinator.adapters.db.purchase-list-management-data :as adapters.db.purchase-list-management-data]
            [purchase-listinator.misc.date :as misc.date]))

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
   {:db/ident       :purchase-list/user-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc         "user id"}])

(s/defn ^:private transact
  [connection & purchases-lists]
  (d/transact connection purchases-lists))

(s/defn get-enabled
  [allowed-lists-ids :- [s/Uuid]
   {:keys [connection]}]
  (->> (d/q '[:find [(pull ?e [*]) ...]
              :in $ [?a-l-id ...]
              :where
              [?e :purchase-list/id ?a-l-id]
              [?e :purchase-list/enabled true]]
            (d/db connection) allowed-lists-ids)
       (sort-by :purchase-list/name)
       (map adapter.purchase-list/db->internal)))

(s/defn get-by-name
  [name :- s/Str
   allowed-lists-ids :- [s/Uuid]
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?e [*])
              :in $ ?name [?a-l-id ...]
              :where
              [?e :purchase-list/id ?a-l-id]
              [?e :purchase-list/name ?name]
              [?e :purchase-list/enabled true]]
            (d/db connection) name allowed-lists-ids)
       ffirst
       adapter.purchase-list/db->internal))

(s/defn get-by-id
  [id :- s/Uuid
   allowed-lists :- [s/Uuid]
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?e [*])
              :in $ ?id [?a-l-id ...]
              :where
              [?e :purchase-list/id ?id]
              [?e :purchase-list/id ?a-l-id]]
            (d/db connection) id allowed-lists)
       ffirst
       adapter.purchase-list/db->internal))

(s/defn upsert
  [purchase-list {:keys [connection]}]
  (->> (adapter.purchase-list/internal->db purchase-list)
       (transact connection))
  purchase-list)

(s/defn existent?
  [id :- s/Uuid
   user-id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find ?id
              :in $ ?id ?u-id
              :where
              [?l :purchase-list/id ?id]
              [?l :purchase-list/user-id ?u-id]]
            (d/db connection) id user-id)
       ffirst))

(s/defn disable
  [id :- s/Uuid
   {:keys [connection]}]
  (let [wire {:purchase-list/id      id
              :purchase-list/enabled false}]
    (transact connection wire)
    (adapter.purchase-list/db->internal wire)))

(s/defn get-allowed-lists-by-user-id :- [s/Uuid]
  [user-id :- s/Uuid
   {:keys [connection]}]
  (d/q '[:find [?l-id ...]
         :in $ ?u-id
         :where
         (or-join [?l-id ?u-id]
                  (and [?s :purchase-list-share/customer-id ?u-id]
                       [?s :purchase-list-share/list-id ?l-id])
                  (and [?e :purchase-list/user-id ?u-id]
                       [?e :purchase-list/id ?l-id]))]
       (d/db connection) user-id))

(s/defn get-management-data
  ([purchase-list-id :- s/Uuid
    allowed-lists-ids :- [s/Uuid]
    datomic]
   (get-management-data purchase-list-id allowed-lists-ids (misc.date/numb-now) datomic))
  ([purchase-list-id :- s/Uuid
    allowed-lists-ids :- [s/Uuid]
    moment :- s/Num
    {:keys [connection]}]
   (->> (d/q '[:find (pull ?e [* {[:purchase-category/_purchase-list :as :purchase-list/categories]
                                  [* {[:purchase-item/_category :as :purchase-category/items] [* {:purchase-item/category [:purchase-category/id]}]
                                      :purchase-category/purchase-list                        [:purchase-list/id]}]}])
               :in $ ?id [?a-l-id ...]
               :where
               [?e :purchase-list/id ?id]
               [?e :purchase-list/id ?a-l-id]]
             (d/as-of (d/db connection) (misc.date/numb->date moment)) purchase-list-id allowed-lists-ids)
        ffirst
        (adapters.db.purchase-list-management-data/db->categories+items-view))))
