(ns purchase-listinator.flows.purchase-category
  (:require [schema.core :as s]
            [purchase-listinator.dbs.datomic.purchase-category :as datomic.purchase-category]
            [cats.monad.either :refer [left]]
            [purchase-listinator.misc.either :as either]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.logic.purchase-category :as logic.purchase-category]))


(s/defn create
  [{:keys [id purchase-list-id] :as category} :- models.internal.purchase-category/PurchaseCategory
   datomic]
  (either/try-right
    (if-let [existent-category (datomic.purchase-category/get-by-id id datomic)]
      (println existent-category)
      ; todo: check if there is other category from the same list with the same name
      ; if not, upsert
      #_(left {:status 400
               :error  {:message "[[LIST_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
      (-> (datomic.purchase-category/categories-count purchase-list-id datomic)
          (logic.purchase-category/change-order-position category)
          (datomic.purchase-category/upsert datomic)))))