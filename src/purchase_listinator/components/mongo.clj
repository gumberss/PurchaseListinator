(ns purchase-listinator.components.mongo
  (:require [monger.core :as mg]
            [com.stuartsierra.component :as component]))

(defn connect-by-uri
  [uri]
  (mg/connect-via-uri uri))

(defn connect-by-uri-host-port
  [host port db-name]
  (let [conn (mg/connect {:host host :port port})
        db (mg/get-db conn db-name)]
    {:conn conn
     :db   db}))

(defrecord Mongo [config config-key indexes]
  component/Lifecycle
  (start [this]
    (let [{{:keys [host port db-name uri]} config-key} config
          conn (mg/connect {:host host :port port})
          db (mg/get-db conn db-name)]
      (if uri
        (connect-by-uri uri)
        (connect-by-uri-host-port host port db-name))
      (doseq [db-indexes indexes]
        (db-indexes db))
      (assoc this
        :connection conn
        :db db)))
  (stop [this]
    (mg/disconnect (:connection this))
    (dissoc this :connection :db)))

(defn new-mongo
  [config-key indexes]
  (map->Mongo {:indexes indexes
               :config-key config-key}))


(defrecord MongoFake [config]
  component/Lifecycle
  (start [this]
    ;todo
      this)
  (stop [this]
    (dissoc this :connection :db)))

(defn new-mongo-fake
  []
  (map->MongoFake {}))
