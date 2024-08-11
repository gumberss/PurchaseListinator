(ns purchase-listinator.modules.shopping.adapters.in.cart
  (:require
    [purchase-listinator.modules.shopping.schemas.models.cart :as models.internal.cart]
    [purchase-listinator.modules.shopping.schemas.models.cart-events :as internal.cart-events]
    [purchase-listinator.modules.shopping.schemas.models.shopping-list :as models.internal.shopping-list]
    [purchase-listinator.modules.shopping.schemas.wires.in.cart :as wires.in.cart]
    [schema.core :as s]))

(s/defn wire->internal :- models.internal.cart/Cart
  [wire :- wires.in.cart/Cart]
  wire)

(s/defn ^:private item->change-item-event :- internal.cart-events/ChangeItemEvent
  [{:keys [id quantity quantity-in-cart price ] } :- models.internal.shopping-list/ShoppingItem
   {:keys [purchase-list-id user-id ] :as  shopping} :- models.internal.shopping-list/ShoppingList
   now :- s/Num]
  {:id               id
   :moment           now
   :event-type       :change-item
   :user-id          user-id
   :shopping-id      (:id shopping)
   :item-id          id
   :price            price
   :quantity-changed (- quantity quantity-in-cart)
   :purchase-list-id purchase-list-id})

(s/defn items->change-item-event :- [internal.cart-events/ChangeItemEvent]
  [items :- [models.internal.shopping-list/ShoppingItem]
   shopping :- models.internal.shopping-list/ShoppingList
   now :- s/Num]
  (map #(item->change-item-event % shopping now) items))