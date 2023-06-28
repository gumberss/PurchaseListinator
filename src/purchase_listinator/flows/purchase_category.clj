(ns purchase-listinator.flows.purchase-category
  (:require
    [purchase-listinator.dbs.datomic.purchase-list :as datomic.purchase-list]
    [schema.core :as s]
    [purchase-listinator.dbs.datomic.purchase-category :as datomic.purchase-category]
    [cats.monad.either :refer [left]]
    [purchase-listinator.misc.either :as either]
    [purchase-listinator.models.internal.purchase-list.purchase-category :as models.internal.purchase-category]
    [purchase-listinator.logic.purchase-category :as logic.purchase-category]
    [purchase-listinator.logic.reposition :as logic.reposition]
    [purchase-listinator.publishers.purchase-list-category :as publishers.purchase-list-category]))

(s/defn create
  [{:keys [name purchase-list-id] :as category} :- models.internal.purchase-category/PurchaseCategoryCreation
   user-id :- s/Uuid
   {:keys [datomic rabbitmq]}]
  (either/try-right
    (let [allowed-lists-ids (datomic.purchase-list/get-allowed-lists-by-user-id user-id datomic)]
    (if (datomic.purchase-category/get-by-name purchase-list-id name allowed-lists-ids datomic)
      (left {:status 400
             :error  {:message "[[CATEGORY_WITH_THE_SAME_NAME_ALREADY_EXISTENT]]"}})
      (let [new-category (-> (datomic.purchase-category/categories-count purchase-list-id allowed-lists-ids datomic)
                             (logic.reposition/change-order-position category)
                             (logic.purchase-category/link-with-user user-id))]
        (datomic.purchase-category/upsert new-category datomic)
        (publishers.purchase-list-category/category-created new-category rabbitmq)
        new-category)))))

(s/defn edit
  [{:keys [name color id]} :- models.internal.purchase-category/PurchaseCategory
   user-id :- s/Uuid
   datomic]
  (either/try-right
    (if-let [existent-category (datomic.purchase-category/get-by-id id user-id datomic)]
      (if (or (not= name (:name existent-category))
              (not= color (:color existent-category)))
        (-> (assoc existent-category :name name :color color)
            (datomic.purchase-category/upsert datomic))
        existent-category)
      (left {:status 404
             :error  {:message "[[CATEGORY_NOT_FOUND]]"}}))))

(s/defn delete
  [category-id :- s/Uuid
   user-id :- s/Uuid
   datomic
   rabbitmq]
  (either/try-right
    (let [category (datomic.purchase-category/get-by-id category-id user-id datomic)]
      (do (datomic.purchase-category/delete-by-id category-id datomic)
          (publishers.purchase-list-category/category-deleted category rabbitmq)))))

(s/defn change-categories-order
  [category-id :- s/Uuid
   new-position :- s/Num
   user-id :- s/Uuid
   datomic]
  (either/try-right
    (let [{:keys [order-position]} (datomic.purchase-category/get-by-id category-id user-id datomic)
          start-position (min order-position new-position)
          end-position (max order-position new-position)
          repositioned-categories (->> (datomic.purchase-category/get-by-position-range category-id start-position end-position user-id datomic)
                                       logic.purchase-category/sort-by-position
                                       (logic.reposition/reposition order-position new-position))]
      (datomic.purchase-category/upsert-many repositioned-categories datomic))))



