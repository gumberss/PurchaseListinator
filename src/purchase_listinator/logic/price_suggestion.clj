(ns purchase-listinator.logic.price-suggestion
  (:require
    [purchase-listinator.models.internal.price-suggestion :as models.internal.price-suggestion]
    [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
    [purchase-listinator.models.internal.shopping-list :as models.internal.shopping-list]
    [schema.core :as s]))

(s/defn ->cart-event :- models.internal.shopping-cart/ItemPriceSuggested
  [event-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [shopping-id]} :- models.internal.shopping-list/ShoppingList
   {:keys [item-id predicted-date suggested-price]} :- models.internal.price-suggestion/ShoppingItemSuggestedPrice]
  {:id          event-id
   :event-type  :item-price-suggested
   :user-id     user-id
   :moment      predicted-date
   :shopping-id shopping-id
   :item-id     item-id
   :price       suggested-price})