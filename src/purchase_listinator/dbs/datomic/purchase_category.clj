(ns purchase-listinator.dbs.datomic.purchase-category
  (:require [schema.core :as s]
            [datomic.api :as d]
            [purchase-listinator.adapters.db.purchase-category :as adapters.db.purchase-category]))

(def schema
  [{:db/ident       :purchase-category/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "The purchase-category id"}
   {:db/ident       :purchase-category/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "The purchase-category name"}
   {:db/ident       :purchase-category/order-position
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "The purchase-category order postion"}
   {:db/ident       :purchase-category/color
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "The purchase-category color"}
   {:db/ident       :purchase-category/purchase-list
    :db/cardinality :db.cardinality/one
    :db/valueType   :db.type/ref}])

(s/defn ^:private transact
  [connection & purchases-categories]
  @(d/transact connection purchases-categories))


(s/defn categories-count
  [purchase-list-id :- s/Uuid
   {:keys [connection]}]
  (-> (d/q '[:find (count ?e)
             :in $ ?purchase-list-id
             :where
             [?purchase-list :purchase-list/id ?purchase-list-id]
             [?e :purchase-category/purchase-list ?purchase-list]]
           (d/db connection) purchase-list-id)
      ffirst))

(s/defn get-by-id
  [id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?e [* {:purchase-category/purchase-list [*]}])
              :in $ ?id
              :where
              [?e :purchase-category/id ?id]]
            (d/db connection) id)
       ffirst
       adapters.db.purchase-category/db->internal))

(s/defn upsert
  [purchase-category
   {:keys [connection]}]
  (->> (adapters.db.purchase-category/internal->db purchase-category)
       (transact connection))
  purchase-category)