(ns purchase-listinator.logic.shopping-purchase-list-cart-event
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.cart-events :as internal.cart-events]
            [purchase-listinator.models.internal.shopping-list :as models.internal.shopping-list]))

(s/defn created->category :- models.internal.shopping-list/ShoppingListCategory
  [{:keys [category-id] :as event} :- internal.cart-events/PurchaseListCategoryCreated]
  (-> event
      (assoc :id category-id
             :items [])
      (dissoc :category-id :event-type :moment :shopping-id)))

(s/defn created->item :- models.internal.shopping-list/ShoppingItem
  [{:keys [item-id] :as event} :- internal.cart-events/PurchaseListItemCreated]
  (-> event
      (assoc :id item-id :price 0)
      (dissoc :item-id :event-type :moment :shopping-id)))

