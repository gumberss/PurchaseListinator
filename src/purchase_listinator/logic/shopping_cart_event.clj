(ns purchase-listinator.logic.shopping-cart-event
  (:require [purchase-listinator.models.internal.shopping-list :as models.internal.shopping-list]
            [purchase-listinator.models.internal.cart-events :as internal.cart-events]
            [purchase-listinator.logic.shopping-item :as logic.shopping-item]
            [purchase-listinator.logic.shopping-purchase-list-cart-event :as logic.shopping-purchase-list-cart-event]
            [purchase-listinator.logic.reposition :as logic.reposition]
            [purchase-listinator.logic.shopping :as logic.shopping]
            [schema.core :as s]))

(defmulti apply-event (fn [{:keys [event-type]} _] event-type))

(s/defn replace-item :- models.internal.shopping-list/ShoppingListCategory
  [{:keys [items] :as category} :- models.internal.shopping-list/ShoppingListCategory
   {:keys [id] :as new-item} :- models.internal.shopping-list/ShoppingItem]
  (assoc category :items (-> (filter #(not= id (:id %)) items)
                             (conj new-item))))

(s/defn replace-category :- models.internal.shopping-list/ShoppingList
  [{:keys [categories] :as shopping-list} :- models.internal.shopping-list/ShoppingList
   {:keys [id] :as new-category} :- models.internal.shopping-list/ShoppingListCategory]
  (assoc shopping-list :categories (-> (filter #(not= id (:id %)) categories)
                                       (conj new-category))))

(s/defmethod ^:private apply-event :change-item :- models.internal.shopping-list/ShoppingList
  [{:keys [item-id price quantity-changed]} :- internal.cart-events/ChangeItemEvent
   {:keys [categories] :as shopping-list} :- models.internal.shopping-list/ShoppingList]
  (let [{:keys [category-id] :as item} (->> (mapcat :items categories)
                                            (filter #(= item-id (:id %)))
                                            first)
        category (first (filter #(= category-id (:id %)) categories))
        changed-item (logic.shopping-item/update-item item price quantity-changed)
        changed-category (replace-item category changed-item)]
    (replace-category shopping-list changed-category)))

(s/defmethod ^:private apply-event :item-price-suggested :- models.internal.shopping-list/ShoppingList
  [{:keys [item-id price]} :- internal.cart-events/ItemPriceSuggested
   {:keys [categories] :as shopping-list} :- models.internal.shopping-list/ShoppingList]
  (let [{:keys [category-id] :as item} (->> (mapcat :items categories)
                                            (filter #(= item-id (:id %)))
                                            first)
        category (first (filter #(= category-id (:id %)) categories))
        changed-item (logic.shopping-item/update-item-price item price)
        changed-category (replace-item category changed-item)]
    (replace-category shopping-list changed-category)))

(s/defmethod ^:private apply-event :reorder-category :- models.internal.shopping-list/ShoppingList
  [{:keys [category-id new-position]} :- internal.cart-events/ReorderCategoryEvent
   {:keys [categories] :as shopping} :- models.internal.shopping-list/ShoppingList]
  (let [current-position (->> categories
                              (filter #(= (-> % :id) category-id))
                              first
                              :order-position)]
    (assoc shopping :categories (logic.reposition/reposition current-position new-position categories))))

(s/defn reorder-item-same-category
  [shopping :- models.internal.shopping-list/ShoppingList
   category :- models.internal.shopping-list/ShoppingListCategory
   current-position :- s/Num
   new-position :- s/Num]
  (let [old-category (assoc category :items (->> (:items category)
                                                 (logic.reposition/reposition current-position new-position)))]
    (-> (:categories shopping)
        (->> (filter #(not= category %)))
        (conj old-category)
        (->> (assoc shopping :categories)))))

(s/defn remove-item-from-old-category
  [{:keys [items] :as category} :- models.internal.shopping-list/ShoppingListCategory
   {:keys [order-position] :as item} :- models.internal.shopping-list/ShoppingItem]
  (let [max-position (-> (apply max-key :order-position items) :order-position)
        reordered-items (->> (filter #(not= % item) items)
                             (logic.reposition/reposition order-position max-position))]
    (assoc category :items reordered-items)))

(s/defn add-item-to-new-category
  [{:keys [items] :as category} :- models.internal.shopping-list/ShoppingListCategory
   item :- models.internal.shopping-list/ShoppingItem
   new-position :- s/Num]
  (let [{:keys [order-position] :as item} (assoc item :order-position new-position :category-id (:id category))
        max-position (or (-> (sort-by :order-position items) last :order-position)
                         0)
        reordered-items (-> (logic.reposition/reposition order-position max-position items)
                            (conj (assoc item :order-position new-position)))]
    (assoc category :items reordered-items)))

(s/defn reorder-item-other-category
  [{:keys [categories] :as shopping}
   item :- models.internal.shopping-list/ShoppingItem
   old-category :- models.internal.shopping-list/ShoppingListCategory
   new-category :- models.internal.shopping-list/ShoppingListCategory
   new-position :- s/Num]
  (let [changed-old-category (remove-item-from-old-category old-category item)
        changed-new-category (add-item-to-new-category new-category item new-position)]
    (assoc shopping :categories (->> (remove #{old-category new-category} categories)
                                     (concat [changed-old-category changed-new-category])))))
(s/defn contains-item?
  [item-id :- s/Uuid
   {:keys [items]} :- models.internal.shopping-list/ShoppingListCategory]
  (some #(= item-id (:id %)) items))

(s/defmethod ^:private apply-event :reorder-item :- models.internal.shopping-list/ShoppingList
  [{:keys [new-category-id item-id new-position]} :- internal.cart-events/ReorderItemEvent
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

(s/defmethod ^:private apply-event :purchase-list-category-deleted :- models.internal.shopping-list/ShoppingList
  [{:keys [category-id]} :- internal.cart-events/PurchaseListCategoryDeleted
   {:keys [categories] :as shopping} :- models.internal.shopping-list/ShoppingList]

  (let [{:keys [order-position]} (filter #(= (:id %) category-id) categories)
        reposition-category (partial logic.reposition/decrement-if-after order-position)]
    (->> categories
         (filter #(not= (:id %) category-id))
         (map reposition-category)
         (assoc shopping :categories))))

(s/defmethod ^:private apply-event :purchase-list-category-created :- models.internal.shopping-list/ShoppingList
  [event :- internal.cart-events/PurchaseListCategoryCreated
   {:keys [categories] :as shopping} :- models.internal.shopping-list/ShoppingList]
  (let [category (logic.shopping-purchase-list-cart-event/created->category event)]
    (assoc shopping :categories (conj categories category))))

(s/defmethod ^:private apply-event :purchase-list-item-created :- models.internal.shopping-list/ShoppingList
  [{:keys [category-id] :as event} :- internal.cart-events/PurchaseListItemCreated
   {:keys [categories] :as shopping} :- models.internal.shopping-list/ShoppingList]
  (let [{:keys [items] :as category} (first (filter #(= (:id %) category-id) categories))
        item (logic.shopping-purchase-list-cart-event/created->item event)
        changed-category (assoc-in category [:items] (conj items item))]
    (replace-category shopping changed-category)))

(s/defmethod ^:private apply-event :purchase-list-item-changed :- models.internal.shopping-list/ShoppingList
  [{:keys [item-id quantity name]} :- internal.cart-events/PurchaseListItemChanged
   {:keys [categories] :as shopping} :- models.internal.shopping-list/ShoppingList]
  (let [{:keys [category-id] :as item} (->> (mapcat :items categories)
                                            (filter #(= item-id (:id %)))
                                            first)
        category (first (filter #(= category-id (:id %)) categories))
        changed-item (assoc item :quantity quantity :name name)
        changed-category (replace-item category changed-item)]
    (replace-category shopping changed-category)))

(s/defmethod ^:private apply-event :purchase-list-item-deleted :- models.internal.shopping-list/ShoppingList
  [{:keys [category-id item-id]} :- internal.cart-events/PurchaseListItemDeleted
   {:keys [categories] :as shopping} :- models.internal.shopping-list/ShoppingList]
  (let [{:keys [items] :as category} (->> categories
                                          (filter #(= (:id %) category-id))
                                          (first))
        changed-category (assoc-in category [:items] (filter #(not= (:id %) item-id) items))]
    (replace-category shopping changed-category)))

(s/defmethod ^:private apply-event :default :- models.internal.shopping-list/ShoppingList
  [{:keys [event-type]} :- internal.cart-events/CartEvent
   shopping :- models.internal.shopping-list/ShoppingList]
  (println "Not found apply-event function for " event-type " event type")
  shopping)

(s/defn ^:private apply-events
  [[current & remaining] :- (s/maybe [internal.cart-events/CartEvent])
   shopping :- models.internal.shopping-list/ShoppingList]
  (if (not current)
    shopping
    (recur remaining (apply-event current shopping))))

(s/defn apply-cart :- models.internal.shopping-list/ShoppingList
  [{:keys [events]} :- (s/maybe internal.cart-events/Cart)
   shopping :- models.internal.shopping-list/ShoppingList]
  (-> (apply-events (sort-by :moment events) shopping)
      logic.shopping/sort-shopping-data))
