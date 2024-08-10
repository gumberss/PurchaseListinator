(ns purchase-listinator.modules.shopping.logic.shopping
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping.schemas.models.shopping :as models.internal.shopping]
            [purchase-listinator.modules.shopping.schemas.models.shopping-list :as models.internal.shopping-list]
            [purchase-listinator.modules.shopping.schemas.models.shopping-initiation :as models.internal.shopping-initiation]
            [purchase-listinator.modules.shopping.schemas.models.shopping-category :as models.internal.shopping-category]
            [purchase-listinator.modules.shopping.schemas.models.shopping-item :as models.internal.shopping-item]))

(s/defn initiation->shopping :- models.internal.shopping/Shopping
  [shopping :- models.internal.shopping-initiation/ShoppingInitiation
   now :- s/Num]
  (-> (assoc shopping :status :in-progress
                      :date now)
      (select-keys (keys models.internal.shopping/Shopping))))

(s/defn purchase-list->shopping-list :- models.internal.shopping-list/ShoppingList
  [shopping-id :- s/Uuid
   purchase-list :- models.internal.shopping-list/ManagementData]
  (assoc purchase-list :shopping-id shopping-id))

(s/defn link-with-user :- models.internal.shopping/Shopping
  [shopping :- models.internal.shopping/Shopping
   user-id :- s/Uuid]
  (assoc shopping :user-id user-id))

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
   categories :- [models.internal.shopping-category/ShoppingCategory]]
  (assoc shopping :categories categories))

(s/defn fill-quantity-in-cart-empty :- models.internal.shopping-item/ShoppingItem
  [{:keys [quantity-in-cart] :as item} :- models.internal.shopping-item/ShoppingItem]
  (assoc item :quantity-in-cart (or quantity-in-cart 0)))

(s/defn fill-item-quantity-in-cart-empty :- models.internal.shopping-category/ShoppingCategory
  [{:keys [items] :as category} :- models.internal.shopping-category/ShoppingCategory]
  (assoc category :items (map fill-quantity-in-cart-empty items)))

(s/defn fill-items-empty-quantity-in-cart :- models.internal.shopping/Shopping
  [categories :- [models.internal.shopping-category/ShoppingCategory]]
  (map fill-item-quantity-in-cart-empty categories))

(s/defn finish :- models.internal.shopping/Shopping
  [shopping :- models.internal.shopping/Shopping]
  (assoc shopping :status :done))