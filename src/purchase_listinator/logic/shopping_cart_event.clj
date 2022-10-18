(ns purchase-listinator.logic.shopping-cart-event
  (:require [purchase-listinator.models.internal.shopping-management-data :as models.internal.shopping-management-data]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
            [schema.core :as s]))

(defmulti apply-event (fn [{:keys [event-type]} _] event-type))
(s/defmethod apply-event :add-item
  [cart-event :- models.internal.shopping-cart/CartEvent
   shopping :- models.internal.shopping-management-data/ManagementData]
  shopping)

(s/defn ^:private apply-events
  [events :- [models.internal.shopping-cart/CartEvent]
   shopping :- models.internal.shopping-management-data/ManagementData]
  (if (empty? events)
    shopping
    (recur (rest events) (apply-event (first events) shopping))))

(s/defn apply-cart :- models.internal.shopping-management-data/ManagementData
  [{:keys [events]} :- models.internal.shopping-cart/Cart
   shopping :- models.internal.shopping-management-data/ManagementData]
  (apply-events events shopping))



