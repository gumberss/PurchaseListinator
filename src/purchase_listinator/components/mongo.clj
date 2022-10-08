(ns purchase-listinator.components.mongo
  (:require [monger.core :as mg]
            [com.stuartsierra.component :as component]))

(def indexes (vector purchase-listinator.dbs.mongo.shopping-location/indexes))

(defrecord Mongo [config]
  component/Lifecycle
  (start [this]
    (let [{{:keys [host port db-name]} :mongo} config
          conn (mg/connect {:host host :port port})
          db (mg/get-db conn db-name)]
      (doseq [db-indexes indexes]
        (db-indexes db))
      (assoc this
        :connection conn
        :db db)))
  (stop [this]
    (mg/disconnect (:connection this))
    (dissoc this :connection :db)))

(defn new-mongo
  []
  (map->Mongo {}))
