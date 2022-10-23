(ns purchase-listinator.logic.shopping-cart-event
  (:require [purchase-listinator.models.internal.shopping-list :as models.internal.shopping-list]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
            [purchase-listinator.logic.reposition :as logic.reposition]
            [schema.core :as s]))

(defmulti apply-event (fn [{:keys [event-type]} _] event-type))

(s/defmethod ^:private apply-event :add-item :- models.internal.shopping-list/ShoppingList
  [cart-event :- models.internal.shopping-cart/CartEvent
   shopping :- models.internal.shopping-list/ShoppingList]
  shopping)

(s/defmethod ^:private apply-event :reorder-category :- models.internal.shopping-list/ShoppingList
  [{:keys [category-id new-position]} :- models.internal.shopping-cart/ReorderCategoryEvent
   {:keys [categories] :as shopping} :- models.internal.shopping-list/ShoppingList]
  (let [current-position (->> categories
                              (map-indexed vector)
                              (filter #(= (-> % second :id) category-id))
                              ffirst)]
    (assoc shopping :categories (logic.reposition/reposition current-position new-position categories))))

(s/defmethod ^:private apply-event :reorder-item :- models.internal.shopping-list/ShoppingList
  [cart-event :- models.internal.shopping-cart/CartEvent
   shopping :- models.internal.shopping-list/ShoppingList]
  shopping)

(s/defmethod ^:private apply-event :default :- models.internal.shopping-list/ShoppingList
  [{:keys [event-type]} :- models.internal.shopping-cart/CartEvent
   shopping :- models.internal.shopping-list/ShoppingList]
  (println "Not found apply-event function for " event-type " event type")
  shopping)

(s/defn ^:private apply-events
  [[current & remaining] :- (s/maybe [models.internal.shopping-cart/CartEvent])
   shopping :- models.internal.shopping-list/ShoppingList]
  (if (not current)
    shopping
    (recur remaining (apply-event current shopping))))

(s/defn apply-cart :- models.internal.shopping-list/ShoppingList
  [{:keys [events]} :- (s/maybe models.internal.shopping-cart/Cart)
   shopping :- models.internal.shopping-list/ShoppingList]
  (apply-events events shopping))

(s/defn add-event :- models.internal.shopping-cart/Cart
  [{:keys [events] :as cart} :- models.internal.shopping-cart/Cart
   event :- models.internal.shopping-cart/CartEvent]
  (assoc cart :events (conj events event)))

