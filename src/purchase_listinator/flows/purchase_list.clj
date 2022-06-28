(ns purchase-listinator.flows.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
            [purchase-listinator.logic.purchase-list :as logic.purchase-list]))

(s/defn get-lists
  [datomic]
  (datomic.purchase-list/get-enabled datomic))

(s/defn create-list [purchase-list datomic]
  (try
    (-> (logic.purchase-list/fill-default-creation-values purchase-list)
        (datomic.purchase-list/create datomic))
    (catch Exception e
      (println e))))