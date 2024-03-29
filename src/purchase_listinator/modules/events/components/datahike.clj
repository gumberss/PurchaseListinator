(ns purchase-listinator.modules.events.components.datahike
  (:require [com.stuartsierra.component :as component]
            [purchase-listinator.modules.events.diplomat.db.shopping-events :as diplomat.db.shopping-events]
            [datahike.api :as d]))

(def schema (concat diplomat.db.shopping-events/schema))

(defn create-schema [conn]
  (d/transact conn schema))

(defrecord Datahike [config-key config]
  component/Lifecycle

  (start [component]
    (try (d/create-database (-> config config-key))
         (catch Exception e
           (println e)))
    (let [conn (d/connect (-> config config-key))]
      (create-schema conn)
      (assoc component :connection conn)))

  (stop [component]
    (d/release (:connection component))
    (dissoc component :connection)))

(defn new-datahike [config-key]
  (map->Datahike {:config-key config-key}))
