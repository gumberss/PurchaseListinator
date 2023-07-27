(ns purchase-listinator.modules.events.diplomat.db.shopping-events
  (:require
    [datahike.api :as d]
    [purchase-listinator.misc.datomic :as misc.datomic]
    [schema.core :as s]
    [purchase-listinator.modules.events.adapters.db.shopping-events :as adapters.db.shopping-events]
    [purchase-listinator.modules.events.schemas.models.cart-events :as models.cart-events]))

(def schema
  [{:db/ident       :shopping-event/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}
   {:db/ident       :shopping-event/moment
    :db/valueType   :db.type/bigint
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/event-type
    :db/valueType   :db.type/keyword
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/user-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/shopping-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/item-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/category-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/properties
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}])

(s/defn upsert
  [events :- [models.cart-events/ShoppingCartEvent]
   {:keys [connection]}]
  (->> (map adapters.db.shopping-events/internal->db events)
       (apply misc.datomic/transact connection))
  events)

(s/defn get-by-user-id :- [models.cart-events/ShoppingCartEvent]
  [user-id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find [(pull ?e [*]) ...]
              :in $ ?u-id
              :where
              [?e :shopping-event/user-id ?u-id]]
            (d/db connection) user-id)
       (map adapters.db.shopping-events/db->internal)))

(s/defn get-by-item-id :- [models.cart-events/ShoppingCartEvent]
  [item-id :- s/Uuid
   {:keys [connection]}]
  (->> (d/q '[:find [(pull ?e [*]) ...]
              :in $ ?i-id
              :where
              [?e :shopping-event/item-id ?i-id]]
            (d/db connection) item-id)
       (map adapters.db.shopping-events/db->internal)))

(s/defn get-by-items-ids :- [models.cart-events/ShoppingCartEvent]
  [items-ids :- [s/Uuid]
   {:keys [connection]}]
  (->> (d/q '[:find [(pull ?e [*]) ...]
              :in $ [?i-id ...]
              :where
              [?e :shopping-event/item-id ?i-id]]
            (d/db connection) items-ids)
       (map adapters.db.shopping-events/db->internal)))