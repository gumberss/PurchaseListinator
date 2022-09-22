(ns purchase-listinator.flows.purchase-item
  (:require [schema.core :as s]
            [cats.monad.either :refer [left]]
            [purchase-listinator.misc.either :as either]
            [purchase-listinator.models.internal.purchase-item :as models.internal.purchase-item]
            [purchase-listinator.logic.purchase-item :as logic.purchase-item]
            [purchase-listinator.dbs.datomic.purchase-item :as datomic.purchase-item]
            [purchase-listinator.logic.reposition :as logic.reposition]))

(s/defn create
  [purchase-list-id :- s/Uuid
   purchase-category-id :- s/Uuid
   {:keys [name] :as item} :- models.internal.purchase-item/PurchaseItem
   datomic]
  (either/try-right
    (if-let [existent-item (datomic.purchase-item/get-by-name name purchase-list-id datomic)]
      (do (println existent-item)
          (left {:status 400
                 :error  {:message "[[ITEM_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}}))
      (-> (datomic.purchase-item/items-count purchase-list-id purchase-category-id datomic)
          (logic.purchase-item/change-order-position item)
          (datomic.purchase-item/upsert purchase-category-id datomic)))))

(s/defn change-items-order-inside-same-category
  [category-id :- s/Uuid
   old-position :- s/Num
   new-position :- s/Num
   datomic]
  (let [start-position (min old-position new-position)
        end-position (max old-position new-position)
        repositioned-items (->> (datomic.purchase-item/get-by-position-range category-id start-position end-position datomic)
                                     logic.purchase-item/sort-by-position
                                     (logic.reposition/reposition old-position new-position))]
    (datomic.purchase-item/upsert-many repositioned-items datomic)))

(s/defn insert-existent-item-in-another-category
  [old-category-id :- s/Uuid
   new-category-id :- s/Uuid
   old-position :- s/Num
   new-position :- s/Num
   datomic]
  (let [old-category-items-to-change-position (datomic.purchase-item/get-by-position-start old-category-id old-position datomic)
        new-category-items-to-change-position (datomic.purchase-item/get-by-position-start new-category-id new-position datomic)
        item-changed (first (filter old-category-items-to-change-position #(= old-position (:order-position %))))]
    (datomic.purchase-item/delete item-changed datomic)
    (datomic.purchase-item/upsert item-changed new-category-id datomic)))

(s/defn change-items-order
  [old-category-id :- s/Uuid
   new-category-id :- s/Uuid
   old-position :- s/Num
   new-position :- s/Num
   datomic]
  (either/try-right
    (if (= old-category-id new-category-id)
      (change-items-order-inside-same-category new-category-id old-position new-position datomic)
      (insert-existent-item-in-another-category old-category-id new-category-id old-position new-position datomic))))



