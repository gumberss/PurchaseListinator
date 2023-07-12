(ns purchase-listinator.modules.shopping-cart.logic.price-suggestion
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
            [purchase-listinator.modules.shopping-cart.schemas.internal.price-suggestion :as internal.price-suggestion]))

(s/defn ->cart-event :- internal.cart-events/ItemPriceSuggested
  [event-id :- s/Uuid
   list-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [item-id predicted-date suggested-price]} :- internal.price-suggestion/ShoppingItemSuggestedPrice]
  {:id               event-id
   :event-type       :price-suggested
   :user-id          user-id
   :moment           predicted-date
   :item-id          item-id
   :price            suggested-price
   :purchase-list-id list-id})