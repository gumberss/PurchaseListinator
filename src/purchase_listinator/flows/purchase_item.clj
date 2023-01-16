(ns purchase-listinator.flows.purchase-item
  (:require [schema.core :as s]
            [cats.monad.either :refer [left]]
            [purchase-listinator.misc.either :as either]
            [purchase-listinator.models.internal.purchase-list.purchase-item :as models.internal.purchase-item]
            [purchase-listinator.logic.purchase-item :as logic.purchase-item]
            [purchase-listinator.dbs.datomic.purchase-item :as datomic.purchase-item]
            [purchase-listinator.logic.reposition :as logic.reposition]
            [purchase-listinator.publishers.purchase-list-items :as publishers.purchase-list-items]
            [purchase-listinator.models.internal.purchase-list.shopping :as models.internal.purchase-list.shopping]))

(s/defn create
  [{:keys [name category-id] :as item} :- models.internal.purchase-item/PurchaseItem
   datomic
   rabbitmq]
  (either/try-right
    (if-let [existent-item (datomic.purchase-item/get-by-name name category-id datomic)]
      (do (println existent-item)
          (left {:status 400
                 :error  {:message "[[ITEM_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}}))
      (-> (datomic.purchase-item/items-count category-id datomic)
          (logic.purchase-item/change-order-position item)
          (datomic.purchase-item/upsert datomic)
          (publishers.purchase-list-items/item-created rabbitmq)))))

(s/defn delete
  [item-id :- s/Uuid
   datomic
   rabbitmq]
  (either/try-right
    (let [item (datomic.purchase-item/get-by-id item-id datomic)]
      (datomic.purchase-item/delete-by-id item-id datomic)
      (publishers.purchase-list-items/item-deleted item rabbitmq))))

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
  [{:keys [id category-id order-position] :as item} :- models.internal.purchase-item/PurchaseItem
   new-category-id :- s/Uuid
   new-position :- s/Num
   datomic]
  (let [old-category-items-changed (->> (datomic.purchase-item/get-by-position-start category-id order-position datomic)
                                        (filter #(not= id (:id %)))
                                        (map logic.reposition/decrement-order))
        new-category-items-changed (->> (datomic.purchase-item/get-by-position-start new-category-id new-position datomic)
                                        (map logic.reposition/increment-order))
        changed-item (assoc item :category-id new-category-id :order-position new-position)]

    (-> (concat old-category-items-changed
                new-category-items-changed
                [changed-item])
        (datomic.purchase-item/upsert-many datomic))))

(s/defn change-items-order
  [item-id :- s/Uuid
   new-category-id :- s/Uuid
   new-position :- s/Num
   datomic]
  (either/try-right
    (let [{:keys [category-id order-position] :as item} (datomic.purchase-item/get-by-id item-id datomic)]
      (if (= category-id new-category-id)
        (change-items-order-inside-same-category new-category-id order-position new-position datomic)
        (insert-existent-item-in-another-category item new-category-id new-position datomic)))))

(s/defn edit-name
  [item-id :- s/Uuid
   new-name :- s/Str
   datomic
   rabbitmq]
  (either/try-right
    (let [{:keys [name] :as item} (datomic.purchase-item/get-by-id item-id datomic)]
      (if (not= new-name name)
        (-> (assoc item :name new-name)
            (datomic.purchase-item/upsert datomic)
            (publishers.purchase-list-items/item-changed rabbitmq))))))

(s/defn change-item-quantity
  [item-id :- s/Uuid
   new-quantity :- s/Num
   datomic
   rabbitmq]
  (either/try-right
    (let [{:keys [quantity] :as item} (datomic.purchase-item/get-by-id item-id datomic)]
      (if (= quantity new-quantity)
        item
        (-> (assoc item :quantity new-quantity)
            (datomic.purchase-item/upsert datomic)
            (publishers.purchase-list-items/item-changed rabbitmq))))))

(s/defn receive-shopping-finished
  [{:keys [categories]} :- models.internal.purchase-list.shopping/Shopping
   {:keys [datomic]}]
  (let [shopping-items (mapcat :items categories)
        purchase-items (datomic.purchase-item/get-by-ids (map :id shopping-items) datomic)
        new-purchase-items (map (partial logic.purchase-item/find-item-and-update purchase-items) shopping-items)
        items-pair (logic.purchase-item/build-items-pair purchase-items new-purchase-items)]
    (datomic.purchase-item/update-item-quantity items-pair datomic)))
