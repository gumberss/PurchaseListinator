(ns purchase-listinator.flows.purchase-category
  (:require [schema.core :as s]
            [purchase-listinator.dbs.datomic.purchase-category :as datomic.purchase-category]
            [cats.monad.either :refer [left]]
            [purchase-listinator.misc.either :as either]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.logic.purchase-category :as logic.purchase-category]))


(s/defn create
  [{:keys [name purchase-list-id] :as category} :- models.internal.purchase-category/PurchaseCategory
   datomic]
  (either/try-right
    (if-let [existent-category (datomic.purchase-category/get-by-name purchase-list-id name datomic)]
      (do (println existent-category)
          (left {:status 400
                 :error  {:message "[[CATEGORY_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}}))
      (-> (datomic.purchase-category/categories-count purchase-list-id datomic)
          (logic.purchase-category/change-order-position category)
          (datomic.purchase-category/upsert datomic)))))

(s/defn change-categories-order
  [list-id :- s/Uuid
   old-position :- s/Num
   new-position :- s/Num
   datomic]
  (either/try-right
    (let [start-position (min old-position new-position)
          end-position (max old-position new-position)]
      (->> (datomic.purchase-category/get-by-position-range list-id start-position end-position datomic)
           (sort-by :order-position)
           (logic.purchase-category/reorder old-position new-position)
           #_(logic.purchase-category/change-order-position order-position)
           #_(datomic.purchase-category/upsert datomic)))))



