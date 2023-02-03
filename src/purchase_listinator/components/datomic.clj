(ns purchase-listinator.components.datomic
  (:require [com.stuartsierra.component :as component]
            [datahike.api :as d]
            [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
            [purchase-listinator.dbs.datomic.purchase-category :as datomic.purchase-category]
            [purchase-listinator.dbs.datomic.purchase-item :as datomic.purchase-item]
            [purchase-listinator.dbs.datomic.shopping :as datomic.shopping]
            [purchase-listinator.dbs.datomic.shopping-event :as dbs.datomic.shopping-events]
            [purchase-listinator.dbs.datomic.shopping-category :as dbs.datomic.shopping-category]
            [purchase-listinator.dbs.datomic.shopping-item :as dbs.datomic.shopping-item]))

(def schema (concat datomic.purchase-list/schema
                    datomic.purchase-category/schema
                    datomic.purchase-item/schema
                    datomic.shopping/schema
                    dbs.datomic.shopping-events/schema
                    dbs.datomic.shopping-item/schema
                    dbs.datomic.shopping-category/schema))

(def cfg {:store {:backend :mem
                  :id      "default"}})

(defn create-schema [conn]
  (d/transact conn schema))

(defrecord Datomic [config]
  component/Lifecycle

  (start [component]
    (try (d/create-database cfg)
         (catch Exception _))
    (let [conn (d/connect cfg)]
      (create-schema conn)
      (assoc component :connection conn)))

  (stop [component]
    (d/release (:connection component))
    (dissoc component :connection)))

(defn new-datomic []
  (map->Datomic {}))
