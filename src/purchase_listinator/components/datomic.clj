(ns purchase-listinator.components.datomic
  (:require [com.stuartsierra.component :as component]
            [datomic.api :as d]
            [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
            [purchase-listinator.dbs.datomic.purchase-category :as datomic.purchase-category]
            [purchase-listinator.dbs.datomic.purchase-item :as datomic.purchase-item]
            [purchase-listinator.dbs.datomic.shopping :as datomic.shopping]
            [purchase-listinator.dbs.datomic.shopping-event :as dbs.datomic.shopping-events]
            [purchase-listinator.dbs.datomic.shopping-category :as dbs.datomic.shopping-category]
            [purchase-listinator.dbs.datomic.shopping-item :as dbs.datomic.shopping-item]))

#_(def db-uri "datomic:free://localhost:4334/datomic-component?password=datomic")

(def schema (concat datomic.purchase-list/schema
                    datomic.purchase-category/schema
                    datomic.purchase-item/schema
                    datomic.shopping/schema
                    dbs.datomic.shopping-events/schema
                    dbs.datomic.shopping-item/schema
                    dbs.datomic.shopping-category/schema))

(defn create-schema [conn]
  @(d/transact conn schema))

(defrecord Datomic [config]
  component/Lifecycle

  (start [component]
    (println  (-> config :datomic :db-uri))
    (d/create-database (-> config :datomic :db-uri))
    (let [conn (d/connect (-> config :datomic :db-uri))]
      (create-schema conn)
      (assoc component :connection conn)))

  (stop [component]
    (d/release (:connection component))
    (dissoc component :connection)))

(defn new-datomic []
  (map->Datomic {}))
