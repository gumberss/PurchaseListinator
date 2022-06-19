(ns purchase-listinator.components.mongo
  (:require [monger.core :as mg]
            [com.stuartsierra.component :as component]
            [monger.collection :as mc]))

(defrecord Mongo [config]
  component/Lifecycle
  (start [this]
    (let [{{:keys [host port db-name]} :mongo} config
          conn (mg/connect {:host host :port port})
          db (mg/get-db conn db-name)]
      (mc/ensure-index db "people" (array-map :latlong "2dsphere"))
      (assoc this
        :connection conn
        :db db)))
  (stop [this]
    (mg/disconnect (:connection this))
    (dissoc this :connection :db)))

(defn new-mongo
  []
  (map->Mongo {}))
