(ns purchase-listinator.modules.shopping-cart.logic.events
  (:require
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [schema.core :as s]))

(s/defn irrelevant-events? :- s/Bool
  [shopping-id :- s/Uuid
   {:keys [event-type] :as event} :- internal.cart-events/CartEvent]
  (let [possible-irrelevant-events #{:reorder-category :reorder-item}]
    (and (contains? possible-irrelevant-events event-type)
         (not= shopping-id (:shopping-id event)))))

(s/defn relevant-by-shopping :- [internal.cart-events/CartEvent]
  [shopping-id :- s/Uuid
   events :- [internal.cart-events/CartEvent]]
  (remove (partial irrelevant-events? shopping-id) events))

(s/defn from-current-shopping?
  [current-shopping-id :- s/Uuid
   {:keys [shopping-id]} :- internal.cart-events/CartEvent]
  (or (nil? shopping-id)
      (= current-shopping-id shopping-id)))

(s/defn filter-by-shopping
  [shopping-id :- s/Uuid
   events :- [internal.cart-events/CartEvent]]
  (->> (relevant-by-shopping shopping-id events)
       (filter (partial from-current-shopping? shopping-id))))