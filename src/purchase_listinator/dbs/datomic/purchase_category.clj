(ns purchase-listinator.dbs.datomic.purchase-category
  (:require [schema.core :as s]
            [datomic.api :as d]
            [purchase-listinator.adapters.db.purchase-category :as adapters.db.purchase-category]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]))

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
  (-> (d/q '[:find (count ?c)
             :in $ ?purchase-list-id
             :where
             [?l :purchase-list/id ?purchase-list-id]
             [?c :purchase-category/purchase-list ?l]]
           (d/db connection) purchase-list-id)
      ffirst))

(s/defn get-by-name :- models.internal.purchase-category/PurchaseCategory
  [purchase-list-id :- s/Uuid
   name :- s/Str
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?c [*])
              :in $ ?list-id ?name
              :where
              [?l :purchase-list/id ?list-id]
              [?c :purchase-category/purchase-list ?l]
              [?c :purchase-category/name ?name]]
            (d/db connection) purchase-list-id name)
       ffirst
       adapters.db.purchase-category/db->internal))

(s/defn get-by-position-range :- [models.internal.purchase-category/PurchaseCategory]
  [list-id :- s/Uuid
   start-range :- s/Num
   end-range :- s/Num
   {:keys [connection]}]
  (->> (d/q '[:find [(pull ?c [*]) ...]
              :in $ ?l-id ?s-range ?e-range
              :where
              [?l :purchase-list/id ?l-id]
              [?l :purchase-list/purchase-categories ?c]
              [?c :purchase-category/order-position ?o]
              [(<= ?s-range ?o)]
              [(<= ?o ?e-range)]]
            (d/db connection) list-id start-range end-range)
       (map adapters.db.purchase-category/db->internal)))

(s/defn upsert :- models.internal.purchase-category/PurchaseCategory
  [purchase-category :- models.internal.purchase-category/PurchaseCategory
   {:keys [connection]}]
  (->> (adapters.db.purchase-category/internal->db purchase-category)
       (transact connection))
  purchase-category)

(s/defn upsert-many :- [models.internal.purchase-category/PurchaseCategory]
  [purchase-categories :- [models.internal.purchase-category/PurchaseCategory]
   {:keys [connection]}]
  (->> purchase-categories
       (mapv adapters.db.purchase-category/internal->db)
       (apply transact connection))
  purchase-categories)
