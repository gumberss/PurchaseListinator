(ns purchase-listinator.modules.shopping-cart.logic.events
  (:require
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [schema.core :as s]))

(s/defn irrelevant-events-for
  [shopping-id :- s/Uuid
   {:keys [event-type] :as event} :- internal.cart-events/CartEvent]
  (let [possible-irrelevant-events #{:reorder-category :reorder-item}]
    (and (contains? possible-irrelevant-events event-type)
         (not= shopping-id (:shopping-id event)))))

(s/defn filter-by-shopping
  [shopping-id :- s/Uuid
   events :- [internal.cart-events/CartEvent]]
  (remove (partial irrelevant-events-for shopping-id)) events)