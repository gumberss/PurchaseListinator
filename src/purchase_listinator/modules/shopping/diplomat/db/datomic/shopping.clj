(ns purchase-listinator.modules.shopping.diplomat.db.datomic.shopping
  (:require [schema.core :as s]
            [datahike.api :as d]
            [purchase-listinator.modules.shopping.adapters.db.datomic.shopping :as adapter.shopping]
            [purchase-listinator.modules.shopping.adapters.db.datomic.shopping-item :as adapter.shopping-item]
            [purchase-listinator.modules.shopping.adapters.db.datomic.shopping-category :as adapter.shopping-category]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.modules.shopping.schemas.models.shopping :as models.shopping]))

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
   {:db/ident       :shopping/list-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping/status
    :db/valueType   :db.type/keyword
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping/user-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc         "user id"}])

(s/defn upsert
  [{:keys [categories] :as shopping} :- models.shopping/Shopping
   {:keys [connection]}]
  (let [items (map adapter.shopping-item/internal->db (mapcat :items categories))
        categories (map adapter.shopping-category/internal->db categories)
        shopping (adapter.shopping/internal->db shopping)]
    (->> (concat [shopping] categories items)
         (apply misc.datomic/transact connection)))
  shopping)


(s/defn get-in-progress-by-list-id :- [models.shopping/Shopping]
  [list-id :- s/Uuid
   allowed-lists-ids :- [s/Uuid]
   {:keys [connection]}]
  (->> (d/q '[:find [(pull ?s [*]) ...]
              :in $ ?l-id [?a-l-id ...]
              :where
              [?s :shopping/list-id ?l-id]
              [?s :shopping/list-id ?a-l-id]
              [?s :shopping/status :in-progress]]
            (d/db connection) list-id allowed-lists-ids)
       (map adapter.shopping/db->internal)))

(s/defn get-by-id :- models.shopping/Shopping
  [id :- s/Uuid
   allowed-lists-ids :- [s/Uuid]
   {:keys [connection]}]
  (->> (d/q '[:find (pull ?s [* {:shopping/list [:purchase-list/id]}])
              :in $ ?id [?a-l-id ...]
              :where
              [?s :shopping/id ?id]
              [?s :shopping/list-id ?a-l-id]]
            (d/db connection) id allowed-lists-ids)
       ffirst
       adapter.shopping/db->internal))
