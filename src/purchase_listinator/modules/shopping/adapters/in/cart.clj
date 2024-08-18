(ns purchase-listinator.modules.shopping.adapters.in.cart
  (:require
    [purchase-listinator.modules.shopping.schemas.models.cart-events :as internal.cart-events]
    [purchase-listinator.modules.shopping.schemas.models.shopping-list :as models.internal.shopping-list]
    [schema.core :as s]))

(s/defn wire->internal :- [internal.cart-events/CartEvent]
  [wire :- [internal.cart-events/CartEvent]]
  wire)

(s/defn ^:private item->change-item-event :- internal.cart-events/ChangeItemEvent
  [{:keys [id quantity quantity-in-cart price]} :- models.internal.shopping-list/ShoppingItem
   {:keys [shopping-id user-id] :as shopping-list} :- models.internal.shopping-list/ShoppingList
   now :- s/Num]
  {:event-id         id
   :moment           now
   :event-type       :change-item
   :user-id          user-id
   :shopping-id      shopping-id
   :item-id          id
   :price            price
   :quantity-changed (- quantity (or quantity-in-cart 0))
   :purchase-list-id (:id shopping-list)})

(s/defn items->change-item-event :- [internal.cart-events/ChangeItemEvent]
  [items :- [models.internal.shopping-list/ShoppingItem]
   shopping :- models.internal.shopping-list/ShoppingList
   now :- s/Num]
  (map #(item->change-item-event % shopping now) items))