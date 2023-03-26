(ns purchase-listinator.components.mongo
  (:require [monger.core :as mg]
            [com.stuartsierra.component :as component]
            [purchase-listinator.dbs.mongo.shopping-location :as dbs.mongo.shopping-location]))

(def indexes (vector dbs.mongo.shopping-location/indexes))

(defn connect-by-uri
  [uri]
  (mg/connect-via-uri uri))

(defn connect-by-uri-host-port
  [host port db-name]
  (let [conn (mg/connect {:host host :port port})
        db (mg/get-db conn db-name)]
    {:conn conn
     :db   db}))

(defrecord Mongo [config]
  component/Lifecycle
  (start [this]
    (let [{{:keys [host port db-name uri]} :mongo} config
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
  []
  (map->Mongo {}))


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
