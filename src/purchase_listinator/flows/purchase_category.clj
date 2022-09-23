(ns purchase-listinator.flows.purchase-category
  (:require [schema.core :as s]
            [purchase-listinator.dbs.datomic.purchase-category :as datomic.purchase-category]
            [cats.monad.either :refer [left]]
            [purchase-listinator.misc.either :as either]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
            [purchase-listinator.logic.purchase-category :as logic.purchase-category]
            [purchase-listinator.logic.reposition :as logic.reposition]))


(s/defn create
  [{:keys [name purchase-list-id] :as category} :- models.internal.purchase-category/PurchaseCategory
   datomic]
  (either/try-right
    (if (datomic.purchase-category/get-by-name purchase-list-id name datomic)
      (left {:status 400
             :error  {:message "[[CATEGORY_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
      (let [new-category (-> (datomic.purchase-category/categories-count purchase-list-id datomic)
                             (logic.reposition/change-order-position category))]
        (datomic.purchase-category/upsert new-category datomic)))))

(s/defn change-categories-order
  [list-id :- s/Uuid
   old-position :- s/Num
   new-position :- s/Num
   datomic]
  (either/try-right
    (let [start-position (min old-position new-position)
          end-position (max old-position new-position)
          repositioned-categories (->> (datomic.purchase-category/get-by-position-range list-id start-position end-position datomic)
                                       logic.purchase-category/sort-by-position
                                       (logic.reposition/reposition old-position new-position))]
      (datomic.purchase-category/upsert-many repositioned-categories datomic))))



