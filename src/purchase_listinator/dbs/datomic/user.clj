(ns purchase-listinator.dbs.datomic.user
  (:require
    [purchase-listinator.misc.datomic :as misc.datomic]
    [schema.core :as s]
    [datahike.api :as d]
    [purchase-listinator.adapters.db.user :as adapters.db.user]))

(def schema
  [{:db/ident       :user/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "The user table id"}
   {:db/ident       :user/external-id
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "The user external identifier"}])

(s/defn existent?
  [external-user-id :- s/Str
   {:keys [connection]}]
  (->> (d/q '[:find ?id
              :in $ ?e-id
              :where
              [?u :user/external-id ?e-id]
              [?u :user/id ?id]]
            (d/db connection) external-user-id)
       ffirst))

(s/defn find-by-external-id
  [external-user-id :- s/Str
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?u [*])
              :in $ ?e-id
              :where
              [?u :user/external-id ?e-id]]
            (d/db connection) external-user-id)
       ffirst
       adapters.db.user/db->internal))

(s/defn upsert
  [user
   {:keys [connection]}]
  (->> (adapters.db.user/internal->db user)
       (misc.datomic/transact connection))
  user)