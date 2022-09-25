(ns purchase-listinator.flows.purchase-category
  (:require [schema.core :as s]
            [purchase-listinator.dbs.datomic.purchase-category :as datomic.purchase-category]
            [cats.monad.either :refer [left]]
            [purchase-listinator.misc.either :as either]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]
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

(s/defn delete
  [item-id :- s/Uuid
   datomic]
  (either/try-right
    (datomic.purchase-category/delete-by-id item-id datomic)))

(s/defn change-categories-order
  [category-id :- s/Uuid
   new-position :- s/Num
   datomic]
  (either/try-right
    (let [{:keys [order-position]} (datomic.purchase-category/get-by-id category-id datomic)
          start-position (min order-position new-position)
          end-position (max order-position new-position)
          repositioned-categories (->> (datomic.purchase-category/get-by-position-range category-id start-position end-position datomic)
                                       logic.purchase-category/sort-by-position
                                       (logic.reposition/reposition order-position new-position))]
      (datomic.purchase-category/upsert-many repositioned-categories datomic))))



