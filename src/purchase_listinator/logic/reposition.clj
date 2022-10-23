(ns purchase-listinator.logic.reposition
  (:require [schema.core :as s]
            [purchase-listinator.models.logic.reposition :as models.logic.reposition]))

(s/defn change-order-position :- models.logic.reposition/Reorder
  [order-position :- s/Int
   reorder :- models.logic.reposition/Reorder]
  (assoc reorder :order-position (or order-position 0)))

(s/defn increment-order :- models.logic.reposition/Reorder
  [{:keys [order-position] :as reorder} :- models.logic.reposition/Reorder]
  (change-order-position (inc order-position) reorder))

(s/defn decrement-order :- models.logic.reposition/Reorder
  [{:keys [order-position] :as reorder} :- models.logic.reposition/Reorder]
  (change-order-position (dec order-position) reorder))

(s/defn decrement-if-after :- models.logic.reposition/Reorder
  [position
   {:keys [order-position] :as item} :- models.logic.reposition/Reorder]
  (if (> order-position position)
    (decrement-order item)
    item))

(s/defn increment-if-after-or-equal :- models.logic.reposition/Reorder
  [position
   {:keys [order-position] :as item} :- models.logic.reposition/Reorder]
  (if (>= order-position position)
    (increment-order item)
    item))

(s/defn ^:private reposition-one :- models.logic.reposition/Reorder
  [old-position :- s/Num
   new-position :- s/Num
   {:keys [order-position] :as reorder} :- models.logic.reposition/Reorder]
  (let [decrease-position? (< old-position new-position)
        the-reorder-reordered (= old-position order-position)
        reposition-way (if decrease-position? decrement-order increment-order)]
    (if the-reorder-reordered
      (change-order-position new-position reorder)
      (reposition-way reorder))))

(s/defn reposition :- [models.logic.reposition/Reorder]
  [old-position :- s/Num
   new-position :- s/Num
   reorder :- [models.logic.reposition/Reorder]]
  (let [reposition-func* (partial reposition-one old-position new-position)
        max-position (max old-position new-position)
        min-position (min old-position new-position)
        to-reoder (filter #(<= min-position (:order-position %) max-position) reorder)
        not-changed (clojure.set/difference (set reorder) (set to-reoder))
        changed (map reposition-func* to-reoder)]
    (sort-by :order-position (concat changed not-changed))))
