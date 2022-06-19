(ns purchase-listinator.components.datomic
  (:require [com.stuartsierra.component :as component]
            [datomic.api :as d]))

(def db-uri "datomic:free://localhost:4334/datomic-component?password=datomic")
(d/create-database db-uri)

(def schema [{:db/ident :person/id
              :db/valueType :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :person/name
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "The name"}])

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