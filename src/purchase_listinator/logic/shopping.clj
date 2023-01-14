(ns purchase-listinator.logic.shopping
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]
            [purchase-listinator.models.internal.purchase-list-management-data :as purchase-list-management-data]
            [purchase-listinator.models.internal.shopping-list :as models.internal.shopping-list]
            [purchase-listinator.models.internal.shopping-initiation :as models.internal.shopping-initiation]
            [purchase-listinator.models.internal.shopping-category :as models.internal.shopping-category]))

(s/defn initiation->shopping :- models.internal.shopping/Shopping
  [shopping :- models.internal.shopping-initiation/ShoppingInitiation
   now :- s/Num]
  (-> (assoc shopping :status :in-progress
                      :date now)
      (select-keys (keys models.internal.shopping/Shopping))))

(s/defn purchase-list->shopping-list :- models.internal.shopping-list/ShoppingList
  [shopping-id :- s/Uuid
   purchase-list :- purchase-list-management-data/ManagementData]
  (assoc purchase-list :shopping-id shopping-id))

(s/defn ^:private sort-items
  [items :- [models.internal.shopping-list/ShoppingItem]]
  (sort-by :order-position items))

(s/defn ^:private sort-categories
  [categories :- [models.internal.shopping-list/ShoppingListCategory]]
  (->> (map #(update % :items sort-items) categories)
       (sort-by :order-position)))

(s/defn sort-shopping-data :- models.internal.shopping-list/ShoppingList
  [shopping :- models.internal.shopping-list/ShoppingList]
  (update shopping :categories sort-categories))

(s/defn fill-shopping-categories :- models.internal.shopping/Shopping
  [shopping :- models.internal.shopping/Shopping
   categories :- models.internal.shopping-category/ShoppingCategory]
  (assoc shopping :categories categories))

(s/defn finish :- models.internal.shopping/Shopping
  [shopping :- models.internal.shopping/Shopping]
  (assoc shopping :status :done))
