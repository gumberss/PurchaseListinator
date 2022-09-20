(ns purchase-listinator.flows.purchase-category
  (:require [schema.core :as s]
            [purchase-listinator.dbs.datomic.purchase-category :as datomic.purchase-category]
            [cats.monad.either :refer [left]]
            [purchase-listinator.misc.either :as either]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
            [purchase-listinator.logic.purchase-category :as logic.purchase-category]))


(s/defn create
  [list-id :- s/Uuid
   {:keys [name] :as category} :- models.internal.purchase-category/PurchaseCategory
   datomic]
  (either/try-right
    (if (datomic.purchase-category/get-by-name list-id name datomic)
      (left {:status 400
             :error  {:message "[[CATEGORY_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
      (let [new-category (-> (datomic.purchase-category/categories-count list-id datomic)
                             (logic.purchase-category/change-order-position category))]
        (datomic.purchase-list/add-category list-id new-category datomic)))))

(s/defn change-categories-order
  [list-id :- s/Uuid
   old-position :- s/Num
   new-position :- s/Num
   datomic]
  (either/try-right
    (let [start-position (min old-position new-position)
          end-position (max old-position new-position)
          repositioned-categories (->> (datomic.purchase-category/get-by-position-range list-id start-position end-position datomic)
                                       (sort-by :order-position)
                                       (logic.purchase-category/reposition old-position new-position))]
      (datomic.purchase-category/upsert-many repositioned-categories datomic))))



