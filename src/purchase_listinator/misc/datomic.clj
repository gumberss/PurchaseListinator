(ns purchase-listinator.misc.datomic
  (:require [clojure.walk :as walk]))

(defn dissoc-db-key
  [entity]
  (if (map? entity)
    (dissoc entity :db/id)
    entity))

(defn datomic->entity [entities]
  (walk/prewalk dissoc-db-key entities))