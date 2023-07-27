(ns purchase-listinator.modules.shopping-cart.logic.cart
  (:require
    [purchase-listinator.misc.date :as misc.date]
    [purchase-listinator.misc.general :as misc.general]
    [purchase-listinator.modules.shopping-cart.logic.price-suggestion :as logic.price-suggestion]
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart :as internal.cart]
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [purchase-listinator.modules.shopping-cart.schemas.internal.price-suggestion :as internal.price-suggestion]
    [purchase-listinator.modules.shopping-cart.schemas.internal.purchase-list :as internal.purchase-list]
    [schema.core :as s]))

(s/defn ->cart-event
  [price-suggestions :- internal.price-suggestion/ShoppingItemSuggestedPrice
   list-id :- s/Uuid
   user-id :- s/Uuid
   item-id :- s/Uuid]
  (let [item-price-suggestion (first (filter #(= item-id (:item-id %)) price-suggestions))
        item-price-suggestion (or item-price-suggestion {:item-id         item-id
                                                         :predicted-date  (misc.date/numb-now)
                                                         :suggested-price 0})]
    (logic.price-suggestion/->cart-event (misc.general/squuid) list-id user-id item-price-suggestion)))

(s/defn ->cart :- internal.cart/Cart
  [list :- internal.purchase-list/PurchaseList
   events :- [internal.cart-events/CartEvent]]
  {:list   list
   :events events})