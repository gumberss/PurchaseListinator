(ns purchase-listinator.flows.purchase-category
  (:require [schema.core :as s]
            [purchase-listinator.dbs.datomic.purchase-category :as datomic.purchase-category]
            [cats.monad.either :refer [left right]]
            [purchase-listinator.misc.either :as either]))


(s/defn create
  [{:keys [id] :as category}
   datomic]
  (either/try-right
    (if-let [existent-category (datomic.purchase-category/get-by-id id datomic)]
      ; todo: check if there is other category from the same list with the same name
      ; if not, upsert
      (left {:status 400
             :error  {:message "[[LIST_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
      (datomic.purchase-category/upsert category datomic))))