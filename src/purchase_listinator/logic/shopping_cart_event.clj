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
                              (filter #(= (-> % :id) category-id))
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
        (->> (filter #(not= category %)))
        (conj old-category)
        (->> (assoc shopping :categories)))))

(s/defn remove-item-from-old-category
  [{:keys [items] :as category}
   {:keys [order-position] :as item}
   ]
  (let [max-position (-> (apply max-key :order-position items) :order-position)
        reordered-items (->> (filter #(not= % item) items)
                             (logic.reposition/reposition order-position max-position))]
    (assoc category :items reordered-items)))

(s/defn add-item-to-new-category
  [{:keys [items] :as category}
   item
   new-position]
  (let [{:keys [order-position] :as item} (assoc item :order-position new-position :category-id (:id category))
        max-position (or (-> (sort-by :order-position items) last :order-position)
                         0)
        reordered-items (-> (logic.reposition/reposition order-position max-position items)
                            (conj (assoc item :order-position new-position)))]
    (assoc category :items reordered-items)))

(s/defn reorder-item-other-category
  [{:keys [categories] :as shopping}
   item
   old-category
   new-category
   new-position]
  (let [changed-old-category (remove-item-from-old-category old-category item)
        changed-new-category (add-item-to-new-category new-category item new-position)]
    (assoc shopping :categories (->> (remove #{old-category new-category} categories)
                                     (concat [changed-old-category changed-new-category])))))
(s/defn contains-item? [item-id
                        {:keys [items]}]
  (some #(= item-id (:id %)) items))

(s/defmethod ^:private apply-event :reorder-item :- models.internal.shopping-list/ShoppingList
  [{:keys [new-category-id item-id new-position]} :- models.internal.shopping-cart/ReorderItemEvent
   {:keys [categories] :as shopping} :- models.internal.shopping-list/ShoppingList]
  (let [old-category (first (filter #(contains-item? item-id %) categories))
        new-category (first (filter #(= (:id %) new-category-id) categories))
        current-item (->> (:items old-category)
                          (filter #(= (:id %) item-id))
                          first)
        current-item-position (-> current-item :order-position)
        same-category? (= (:id old-category) new-category-id)]
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

