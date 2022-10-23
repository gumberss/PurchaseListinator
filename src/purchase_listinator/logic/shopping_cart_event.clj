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
                              (filter #(= (-> % second :id) category-id))
                              first
                              :order-position)]
    (assoc shopping :categories (logic.reposition/reposition current-position new-position categories))))


(s/defn reorder-item-same-category
  [shopping
   category
   current-position
   new-position]
  (let [old-category (assoc category :items (->> (:items category)
                                                 (logic.reposition/reposition current-position new-position)))]
    (-> (:categories shopping)
        (->> (filter #(not= old-category)))
        (conj old-category)
        (->> (assoc shopping :categories)))))



(s/defn reorder-item-other-category
  [{:keys [categories] :as shopping}
   {:keys [current-position] :as item}
   old-category
   new-category
   new-position]
  (let [changed-old-category (->> (:items old-category)
                                  (map (partial logic.reposition/decrement-if-after current-position))
                                  (remove (set item))
                                  (assoc old-category :items))
        changed-new-category (->> (:items new-category)
                                  (map (partial logic.reposition/increment-if-after-or-equal current-position))
                                  (apply vector (assoc item :order-position new-position))
                                  (assoc new-category :items))]
    (assoc shopping :categories (->> (remove #{old-category new-category} categories)
                                     (apply vector changed-old-category changed-new-category)))))

(s/defmethod ^:private apply-event :reorder-item :- models.internal.shopping-list/ShoppingList
  [{:keys [old-category-id new-category-id item-id new-position]} :- models.internal.shopping-cart/ReorderItemEvent
   {:keys [items] :as shopping} :- models.internal.shopping-list/ShoppingList]
  (let [old-category (first (filter #(= (:id %) old-category-id)))
        new-category (first (filter #(= (:id %) new-category-id)))
        current-item (->> items
                          (filter #(= (-> % second :id) item-id))
                          first)
        current-item-position (-> current-item :order-position)
        same-category? (= old-category-id new-category-id)]
    (if same-category?
      (reorder-item-same-category shopping old-category current-item-position new-position)
      (reorder-item-other-category shopping current-item old-category new-category new-position))))

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
  (apply-events (sort-by :moment events) shopping))

(s/defn add-event :- models.internal.shopping-cart/Cart
  [{:keys [events] :as cart} :- models.internal.shopping-cart/Cart
   event :- models.internal.shopping-cart/CartEvent]
  (assoc cart :events (conj events event)))

