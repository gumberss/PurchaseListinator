(ns purchase-listinator.dbs.datomic.purchase-item
  (:require [schema.core :as s]
            [datahike.api :as d]
            [purchase-listinator.adapters.db.purchase-item :as adapters.db.purchase-item]
            [purchase-listinator.models.internal.purchase-list.purchase-item :as models.internal.purchase-item]
            [purchase-listinator.misc.datomic :as misc.datomic]))

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
    :db/doc         "The purchase-item order position"}
   {:db/ident       :purchase-item/category
    :db/cardinality :db.cardinality/one
    :db/valueType   :db.type/ref}
   {:db/ident       :purchase-item/user-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc         "user id"}])

(s/defn ^:private retract
  [connection & purchases-items-ids]
  (d/transact connection (mapv #(vector :db.fn/retractEntity [:purchase-item/id %]) purchases-items-ids)))

(s/defn items-count
  [purchase-category-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [connection]}]
  (-> (d/q '[:find (count ?i)
             :in $ ?purchase-category-id ?u-id
             :where
             [?c :purchase-category/id ?purchase-category-id]
             [?i :purchase-item/category ?c]
             [?i :purchase-item/user-id ?u-id]]
           (d/db connection) purchase-category-id user-id)
      ffirst))

(s/defn get-by-name :- (s/maybe models.internal.purchase-item/PurchaseItem)
  [name :- s/Str
   category-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?i [*])
              :in $ ?c-id ?name ?u-id
              :where
              [?c :purchase-category/id ?c-id]
              [?c :purchase-category/purchase-list ?l]
              [?all-c :purchase-category/purchase-list ?l]
              [?i :purchase-item/category ?all-c]
              [?i :purchase-item/name ?name]
              [?i :purchase-item/user-id ?u-id]]
            (d/db connection) category-id name user-id)
       ffirst
       adapters.db.purchase-item/db->internal))

(s/defn get-by-id :- models.internal.purchase-item/PurchaseItem
  [item-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?i [* {:purchase-item/category [:purchase-category/id]}])
              :in $ ?i-id ?name ?u-id
              :where
              [?i :purchase-item/id ?i-id]
              [?i :purchase-item/user-id ?u-id]]
            (d/db connection) item-id name user-id)
       ffirst
       adapters.db.purchase-item/db->internal))

(s/defn get-by-ids :- [models.internal.purchase-item/PurchaseItem]
  [item-ids :- [s/Uuid]
   user-id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?i [* {:purchase-item/category [:purchase-category/id]}])
              :in $ [?i-id ...]
              :where
              [?i :purchase-item/id ?i-id]
              [?i :purchase-item/user-id ?u-id]]
            (d/db connection) item-ids user-id)
       flatten
       (map adapters.db.purchase-item/db->internal)))

(s/defn get-by-position-range :- models.internal.purchase-item/PurchaseItems
  [category-id :- s/Uuid
   start-range :- s/Num
   end-range :- s/Num
   user-id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find [(pull ?i [* {:purchase-item/category [:purchase-category/id]}]) ...]
              :in $ ?c-id ?s-range ?e-range ?u-id
              :where
              [?c :purchase-category/id ?c-id]
              [?i :purchase-item/category ?c]
              [?i :purchase-item/user-id ?u-id]
              [?i :purchase-item/order-position ?o]
              [(<= ?s-range ?o)]
              [(<= ?o ?e-range)]]
            (d/db connection) category-id start-range end-range user-id)
       (map adapters.db.purchase-item/db->internal)))

(s/defn get-by-position-start :- models.internal.purchase-item/PurchaseItems
  [category-id :- s/Uuid
   start-range :- s/Num
   user-id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find [(pull ?i [* {:purchase-item/category [:purchase-category/id]}]) ...]
              :in $ ?c-id ?s-range ?u-id
              :where
              [?c :purchase-category/id ?c-id]
              [?i :purchase-item/category ?c]
              [?i :purchase-item/user-id ?u-id]
              [?i :purchase-item/order-position ?o]
              [(<= ?s-range ?o)]]
            (d/db connection) category-id start-range user-id)
       (map adapters.db.purchase-item/db->internal)))

(s/defn delete-by-id :- s/Uuid
  [purchase-item-id :- s/Uuid
   {:keys [connection]}]
  (retract connection purchase-item-id)
  purchase-item-id)

(s/defn upsert :- models.internal.purchase-item/PurchaseItem
  [purchase-item :- models.internal.purchase-item/PurchaseItem
   {:keys [connection]}]
  (->> (adapters.db.purchase-item/internal->db purchase-item)
       (misc.datomic/transact connection))
  purchase-item)

(s/defn upsert-many :- [models.internal.purchase-item/PurchaseItem]
  [purchase-items :- [models.internal.purchase-item/PurchaseItem]
   {:keys [connection]}]
  (->> purchase-items
       (map adapters.db.purchase-item/internal->db)
       (apply misc.datomic/transact connection))
  purchase-items)

(s/defn build-cas [id prop old-value new-value]
  [:db/cas [:purchase-item/id id] prop old-value new-value])

(s/defn build-cas-by-items-pair
  [[old-entity new-entity] :- [models.internal.purchase-item/PurchaseItem]]
  (when (= (:id old-entity) (:id new-entity))
    (build-cas (:id new-entity) :purchase-item/quantity (:quantity old-entity) (:quantity new-entity))))

(s/defn update-item-quantity :- [[models.internal.purchase-item/PurchaseItem]]
  [purchase-items-pair :- [[models.internal.purchase-item/PurchaseItem]]
   {:keys [connection]}]
  (->> (map build-cas-by-items-pair purchase-items-pair)
       (filter identity)
       (apply misc.datomic/transact connection))
  purchase-items-pair)
