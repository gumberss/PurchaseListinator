(ns purchase-listinator.modules.shopping.diplomat.db.datomic.shopping-item
  (:require [schema.core :as s]
            [datahike.api :as d]
            [purchase-listinator.modules.shopping.adapters.db.datomic.shopping-item :as adapters.db.shopping-item]
            [purchase-listinator.modules.shopping.schemas.models.shopping-item :as models.internal.shopping-item]
            [purchase-listinator.misc.datomic :as misc.datomic]))

(def schema
  [{:db/ident       :shopping-item/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "The shopping-item id"}
   {:db/ident       :shopping-item/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "The shopping-item name"}
   {:db/ident       :shopping-item/quantity
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "The shopping-item quantity"}
   {:db/ident       :shopping-item/order-position
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "The shopping-item order position"}
   {:db/ident       :shopping-item/category
    :db/cardinality :db.cardinality/one
    :db/valueType   :db.type/ref}
   {:db/ident       :shopping-item/price
    :db/cardinality :db.cardinality/one
    :db/valueType   :db.type/float}
   {:db/ident       :shopping-item/quantity-in-cart
    :db/cardinality :db.cardinality/one
    :db/valueType   :db.type/long}
   {:db/ident       :shopping-item/user-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc         "user id"}
   {:db/ident       :shopping-item/purchase-list-id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc         "user id"}])

(s/defn upsert :- models.internal.shopping-item/ShoppingItem
  [shopping-item :- models.internal.shopping-item/ShoppingItem
   {:keys [connection]}]
  (->> (adapters.db.shopping-item/internal->db shopping-item)
       (misc.datomic/transact connection))
  shopping-item)

