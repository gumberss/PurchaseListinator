(ns purchase-listinator.dbs.datomic.purchase-item
  (:require [schema.core :as s]
            [datomic.api :as d]
            [purchase-listinator.adapters.db.purchase-item :as adapters.db.purchase-item]
            [purchase-listinator.models.internal.purchase-item :as models.internal.purchase-item]))

(def schema
  [{:db/ident       :purchase-item/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "The purchase-item id"}
   {:db/ident       :purchase-item/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "The purchase-item name"}
   {:db/ident       :purchase-item/quantity
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "The purchase-item quantity"}
   {:db/ident       :purchase-item/order-position
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "The purchase-item order postion"}])

(s/defn ^:private transact
  [connection & purchases-items]
  @(d/transact connection purchases-items))

(s/defn items-count
  [purchase-list-id :- s/Uuid
   purchase-category-id :- s/Uuid
   {:keys [connection]}]
  (-> (d/q '[:find (count ?i)
             :in $ ?purchase-list-id ?purchase-category-id
             :where
             [?purchase-list :purchase-list/id ?purchase-list-id]
             [?purchase-list :purchase-list/purchase-categories ?c]
             [?c :purchase-category/purchase-items ?i]]
           (d/db connection) purchase-list-id purchase-category-id)
      ffirst))

(s/defn get-by-name :- models.internal.purchase-item/PurchaseItem
  [name :- s/Str
   purchase-list-id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?i [*])
              :in $ ?list-id ?name
              :where
              [?l :purchase-list/id ?list-id]
              [?l :purchase-list/purchase-categories ?c]
              [?c :purchase-category/purchase-items ?i]
              [?i :purchase-item/name ?name]]
            (d/db connection) purchase-list-id name)
       ffirst
       adapters.db.purchase-item/db->internal))

(s/defn upsert :- models.internal.purchase-item/PurchaseItem
  [purchase-item :- models.internal.purchase-item/PurchaseItem
   purchase-category-id :- s/Uuid
   {:keys [connection]}]
  (->> (adapters.db.purchase-item/internal->db purchase-category-id purchase-item)
       (transact connection))
  purchase-item)
