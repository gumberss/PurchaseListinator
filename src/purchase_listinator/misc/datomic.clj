(ns purchase-listinator.misc.datomic
  (:require [clojure.walk :as walk]
            [schema.core :as s]
            [datomic.api :as d]))

(defn dissoc-db-key
  [entity]
  (if (map? entity)
    (dissoc entity :db/id)
    entity))

(defn datomic->entity [entities]
  (walk/prewalk dissoc-db-key entities))

(s/defn transact
  [connection & data]
  @(d/transact connection data))


(s/defn retract
  [id-key connection & data]
  @(d/transact connection (mapv #(vector :db.fn/retractEntity [id-key %]) data)))

(s/defn upsert
  [data
   adapter
   {:keys [connection]}]
  (->> (adapter data)
       (transact connection))
  data)
