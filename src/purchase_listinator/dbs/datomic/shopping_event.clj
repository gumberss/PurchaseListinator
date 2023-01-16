(ns purchase-listinator.dbs.datomic.shopping-event
  (:require [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
            [schema.core :as s]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.adapters.db.shopping-event :as adapters.db.shopping-event]))

(def schema
  ; default falues
  [{:db/ident       :shopping-event/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity}
   {:db/ident       :shopping-event/moment
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/event-type
    :db/valueType   :db.type/keyword
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/shopping-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   ; non default values
   {:db/ident       :shopping-event/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/category-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/new-position
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/item-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/new-category-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/price
    :db/valueType   :db.type/float
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/quantity-changed
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/purchase-list-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/color
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/quantity
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :shopping-event/order-position
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}])

(s/defn upsert
  [cart-events :- [models.internal.shopping-cart/CartEvent]
   {:keys [connection]}]
  (->> (map adapters.db.shopping-event/internal->db cart-events)
       (apply misc.datomic/transact connection))
  cart-events)
