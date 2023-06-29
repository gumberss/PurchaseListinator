(ns purchase-listinator.flows.purchase-item
  (:require
    [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
    [schema.core :as s]
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
   user-id :- s/Uuid
   datomic
   rabbitmq]
  (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)]
    (either/try-right
      (if-let [_existent-item (datomic.purchase-item/get-by-name name category-id allowed-lists-ids datomic)]
        (left {:status 400
               :error  {:message "[[ITEM_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
        (-> (datomic.purchase-item/items-count category-id allowed-lists-ids datomic)
            (logic.purchase-item/change-order-position item)
            (logic.purchase-item/link-with-user user-id)
            (datomic.purchase-item/upsert datomic)
            (publishers.purchase-list-items/item-created rabbitmq))))))

(s/defn delete
  [item-id :- s/Uuid
   user-id :- s/Uuid
   datomic
   rabbitmq]
  (either/try-right
    (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)]
      (when-let [item (datomic.purchase-item/get-by-id item-id allowed-lists-ids datomic)]
        (datomic.purchase-item/delete-by-id item-id datomic)
        (publishers.purchase-list-items/item-deleted item rabbitmq)))))

(s/defn change-items-order-inside-same-category
  [category-id :- s/Uuid
   old-position :- s/Num
   new-position :- s/Num
   allowed-lists-ids :- [s/Uuid]
   datomic]
  (let [start-position (min old-position new-position)
        end-position (max old-position new-position)
        repositioned-items (->> (datomic.purchase-item/get-by-position-range category-id start-position end-position allowed-lists-ids datomic)
                                logic.purchase-item/sort-by-position
                                (logic.reposition/reposition old-position new-position))]
    (datomic.purchase-item/upsert-many repositioned-items datomic)))

(s/defn insert-existent-item-in-another-category
  [{:keys [id category-id order-position] :as item} :- models.internal.purchase-item/PurchaseItem
   new-category-id :- s/Uuid
   new-position :- s/Num
   allowed-lists-ids :- [s/Uuid]
   datomic]
  (let [old-category-items-changed (->> (datomic.purchase-item/get-by-position-start category-id order-position allowed-lists-ids datomic)
                                        (filter #(not= id (:id %)))
                                        (map logic.reposition/decrement-order))
        new-category-items-changed (->> (datomic.purchase-item/get-by-position-start new-category-id new-position allowed-lists-ids datomic)
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
   user-id :- s/Uuid
   {:keys [datomic]}]
  (either/try-right
    (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)
          {:keys [category-id order-position] :as item} (datomic.purchase-item/get-by-id item-id allowed-lists-ids datomic)]
      (if (= category-id new-category-id)
        (change-items-order-inside-same-category new-category-id order-position new-position allowed-lists-ids datomic)
        (insert-existent-item-in-another-category item new-category-id new-position allowed-lists-ids datomic)))))

(s/defn edit-name
  [item-id :- s/Uuid
   new-name :- s/Str
   user-id :- s/Uuid
   datomic
   rabbitmq]
  (either/try-right
    (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)
          {:keys [name] :as item} (datomic.purchase-item/get-by-id item-id allowed-lists-ids datomic)]
      (if (not= new-name name)
        (-> (assoc item :name new-name)
            (datomic.purchase-item/upsert datomic)
            (publishers.purchase-list-items/item-changed rabbitmq))))))

(s/defn change-item-quantity
  [item-id :- s/Uuid
   new-quantity :- s/Num
   user-id :- s/Uuid
   {:keys [datomic rabbitmq]}]
  (either/try-right
    (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)
          {:keys [quantity] :as item} (datomic.purchase-item/get-by-id item-id allowed-lists-ids datomic)]
      (if (= quantity new-quantity)
        item
        (-> (assoc item :quantity new-quantity)
            (datomic.purchase-item/upsert datomic)
            (publishers.purchase-list-items/item-changed rabbitmq))))))

(s/defn receive-shopping-finished
  [{:keys [categories user-id]} :- models.internal.purchase-list.shopping/Shopping
   {:keys [datomic]}]
  (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)
        shopping-items (mapcat :items categories)
        purchase-items (datomic.purchase-item/get-by-ids (map :id shopping-items) allowed-lists-ids datomic)
        new-purchase-items (map (partial logic.purchase-item/find-item-and-update-item-quantity purchase-items) shopping-items)
        items-pair (logic.purchase-item/build-items-pair purchase-items new-purchase-items)]
    (datomic.purchase-item/update-item-quantity items-pair datomic)))
