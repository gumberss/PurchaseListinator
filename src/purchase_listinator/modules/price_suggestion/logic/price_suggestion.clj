(ns purchase-listinator.modules.price-suggestion.logic.price-suggestion
  (:require
    [purchase-listinator.modules.price-suggestion.schemas.internal.shopping-item-event :as internal.shopping-item-event]
    [purchase-listinator.modules.price-suggestion.schemas.internal.price-suggestion :as internal.price-suggestion]
    [schema.core :as s]))

(defn week-diff
  [moment moment2]
  (let [diff-ms (- moment2 moment)
        diff-weeks (/ diff-ms (* 1000 60 60 24 7))]
    (Math/floor diff-weeks)))

(s/defn calculate-by-events :- s/Num
  [events :- [internal.shopping-item-event/Event]
   predict-time :- s/Num]
  (let [lasts-by-shopping (->> (filter #(= :change-item (:event-type %)) events)
                               (group-by :shopping-id)
                               (mapcat last)
                               (sort-by :moment))
        least-recent-event (first lasts-by-shopping)
        prices (map :price lasts-by-shopping)
        times (->> (map :moment lasts-by-shopping)
                   (map #(+ 1 (week-diff (:moment least-recent-event) %))))
        _ (println least-recent-event)

        sum-prices (reduce + prices)
        sum-times (reduce + times)
        mean-price (/ sum-prices (count prices))
        mean-time (/ sum-times (count times))
        time-pow-summed (->> (map #(Math/pow % 2) times)
                             (reduce +))
        time-summed-pow (Math/pow (reduce + times) 2)
        price-by-time-summed (->> (map * times prices)
                                  (reduce +))
        divider (- (* (count times) time-pow-summed) time-summed-pow)

        slope (/ (- (* (count times) price-by-time-summed) (* sum-times sum-prices))
                 (if (zero? divider) 1 divider))
        intercept (- mean-price (* slope mean-time))
        predicted-price (+ intercept (* slope predict-time))]
    predicted-price))

(s/defn calculate-for-item :- internal.price-suggestion/ShoppingItemSuggestedPrice
  [predict-date :- s/Num
   {:keys [item-id events]} :- internal.shopping-item-event/ShoppingItemEvent]

  {:item-id         item-id
   :predicted-date  predict-date
   :suggested-price (calculate-by-events events predict-date)})

(s/defn calculate-for-all :- [internal.price-suggestion/ShoppingItemSuggestedPrice]
  [shopping-items-events :- internal.shopping-item-event/ShoppingItemEvents
   predict-date :- s/Num]
  (map (partial calculate-for-item predict-date) shopping-items-events))

