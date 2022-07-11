(ns purchase-listinator.flows.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
            [purchase-listinator.logic.purchase-list :as logic.purchase-list]
            [cats.monad.either :refer [try-either left right]]))

(s/defn get-lists
  [datomic]
  (try-either (datomic.purchase-list/get-enabled datomic)))

(s/defn create-list
  [{:keys [name]}
   datomic]
  (try-either
    (if-let [existent-list (datomic.purchase-list/get-by-name name datomic)]
      (-> existent-list
          logic.purchase-list/disabled?
          (and (datomic.purchase-list/enable (:id existent-list) datomic))
          (or (left {:status 400
                     :error  {:message "[[LIST_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})))
      (-> (logic.purchase-list/generate-new name)
          (datomic.purchase-list/create datomic)))))

(s/defn disable-list
  [id datomic]
  (try-either (datomic.purchase-list/disable id datomic)))