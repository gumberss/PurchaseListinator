(ns purchase-listinator.components.datomic
  (:require [com.stuartsierra.component :as component]
            [datomic.api :as d]
            [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]))

(def db-uri "datomic:free://localhost:4334/datomic-component?password=datomic")
(d/create-database db-uri)

(def schema (conj datomic.purchase-list/schema))

(defn create-schema [conn]
  (d/transact conn schema))

(defrecord Datomic []
  component/Lifecycle

  (start [component]
    (let [conn (d/connect db-uri)]
      (create-schema conn)
      (assoc component :connection conn)))

  (stop [component]
    (dissoc component :connection)))

(defn new-datomic []
  (map->Datomic {}))