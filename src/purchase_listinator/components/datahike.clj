(ns purchase-listinator.components.datahike
  (:require [com.stuartsierra.component :as component]
            [datahike.api :as d]))

(defn create-schema [conn, schemas]
  (d/transact conn schemas))

(defrecord Datahike [config-key schemas config]
  component/Lifecycle

  (start [component]
    (try (d/create-database (-> config config-key))
         (catch Exception e
           (println e)))
    (let [conn (d/connect (-> config config-key))]
      (create-schema conn schemas)
      (assoc component :connection conn)))

  (stop [component]
    (d/release (:connection component))
    (dissoc component :connection)))

(defn new-datahike [config-key schemas]
  (map->Datahike {:config-key config-key
                  :schemas schemas}))
