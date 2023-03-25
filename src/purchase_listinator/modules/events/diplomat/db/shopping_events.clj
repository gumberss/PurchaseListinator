(ns purchase-listinator.modules.events.diplomat.db.shopping-events
  (:require
    [purchase-listinator.misc.datomic :as misc.datomic]
    [schema.core :as s]
    [purchase-listinator.modules.events.schemas.models.shopping-event :as schemas.models.shopping-event]
    [purchase-listinator.modules.events.adapters.db.shopping-events :as adapters.db.shopping-events]))

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
   {:db/ident       :shopping-event/properties
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}])

(s/defn upsert
  [events :- [schemas.models.shopping-event/ShoppingEvent]
   {:keys [connection]}]
  (->> (map adapters.db.shopping-events/internal->db events)
       (apply misc.datomic/transact connection))
  events)