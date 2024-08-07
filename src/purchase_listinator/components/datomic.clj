(ns purchase-listinator.components.datomic
  (:require [com.stuartsierra.component :as component]
            [datahike.api :as d]
            [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
            [purchase-listinator.dbs.datomic.purchase-category :as datomic.purchase-category]
            [purchase-listinator.dbs.datomic.purchase-item :as datomic.purchase-item]
            [purchase-listinator.dbs.datomic.user :as dbs.datomic.user]
            [purchase-listinator.dbs.datomic.share :as dbs.datomic.share]))

(def schema (concat dbs.datomic.user/schema
                    datomic.purchase-list/schema
                    datomic.purchase-category/schema
                    datomic.purchase-item/schema
                    dbs.datomic.share/schema))

(defn create-schema [conn]
  (d/transact conn schema))

(defrecord Datomic [config]
  component/Lifecycle

  (start [component]
    (try (d/create-database (-> config :datomic))
         (catch Exception e
           (println e)))
    (let [conn (d/connect (-> config :datomic))]
      (create-schema conn)
      (assoc component :connection conn)))

  (stop [component]
    (d/release (:connection component))
    (dissoc component :connection)))

(defn new-datomic []
  (map->Datomic {}))
