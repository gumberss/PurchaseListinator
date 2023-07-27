(ns purchase-listinator.modules.shopping-cart.logic.price-suggestion
  (:require
    [purchase-listinator.misc.date :as misc.date]
    [purchase-listinator.misc.general :as misc.general]
    [schema.core :as s]
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [purchase-listinator.modules.shopping-cart.schemas.internal.price-suggestion :as internal.price-suggestion]))

(s/defn ->cart-event :- internal.cart-events/ItemPriceSuggested
  [event-id :- s/Uuid
   list-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [item-id predicted-date suggested-price]} :- internal.price-suggestion/ShoppingItemSuggestedPrice]
  {:id               event-id
   :event-type       :item-price-suggested
   :user-id          user-id
   :moment           predicted-date
   :item-id          item-id
   :price            suggested-price
   :purchase-list-id list-id})

(s/defn generate-cart-event
  [price-suggestions :- internal.price-suggestion/ShoppingItemSuggestedPrice
   list-id :- s/Uuid
   user-id :- s/Uuid
   item-id :- s/Uuid]
  (let [item-price-suggestion (first (filter #(= item-id (:item-id %)) price-suggestions))
        item-price-suggestion (or item-price-suggestion {:item-id         item-id
                                                         :predicted-date  (misc.date/numb-now)
                                                         :suggested-price 0})]
    (->cart-event (misc.general/squuid) list-id user-id item-price-suggestion)))