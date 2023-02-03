(ns purchase-listinator.dbs.datomic.shopping-category
  (:require [schema.core :as s]
            [datahike.api :as d]
            [purchase-listinator.adapters.db.shopping-category :as adapters.db.shopping-category]
            [purchase-listinator.models.internal.shopping-category :as models.internal.shopping-category]))

(def schema
  [{:db/ident       :shopping-category/id
    :db/valueType   :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique      :db.unique/identity
    :db/doc         "The shopping-category id"}
   {:db/ident       :shopping-category/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "The shopping-category name"}
   {:db/ident       :shopping-category/order-position
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "The shopping-category order postion"}
   {:db/ident       :shopping-category/color
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "The shopping-category color"}
   {:db/ident       :shopping-category/shopping
    :db/cardinality :db.cardinality/one
    :db/valueType   :db.type/ref}])

(s/defn ^:private transact
        [connection & purchases-categories]
        (d/transact connection purchases-categories))

(s/defn upsert :- models.internal.shopping-category/ShoppingCategory
        [shopping-category :- models.internal.shopping-category/ShoppingCategory
         {:keys [connection]}]
        (->> (adapters.db.shopping-category/internal->db shopping-category)
             (transact connection))
        shopping-category)

(s/defn upsert-many :- [models.internal.shopping-category/ShoppingCategory]
        [purchase-categories :- [models.internal.shopping-category/ShoppingCategory]
         {:keys [connection]}]
        (->> purchase-categories
             (mapv adapters.db.shopping-category/internal->db)
             (apply transact connection))
        purchase-categories)
