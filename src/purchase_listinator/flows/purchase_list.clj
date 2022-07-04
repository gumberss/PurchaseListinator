(ns purchase-listinator.flows.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
            [purchase-listinator.logic.purchase-list :as logic.purchase-list]
            [cats.monad.either :refer [try-either]]))

(s/defn get-lists
  [datomic]
  (try-either (datomic.purchase-list/get-enabled datomic)))

(s/defn create-list [{:keys [name]} datomic]
  (try-either
    (-> (logic.purchase-list/generate-new name)
        (datomic.purchase-list/create datomic))))