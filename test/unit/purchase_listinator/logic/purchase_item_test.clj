(ns purchase-listinator.logic.purchase-item-test
  (:require [clojure.test :refer :all]
            [schema.test :as s]
            [purchase-listinator.logic.purchase-item :as logic.purchase-item]))

(def category-id (random-uuid))

(def first-item
  {:id             (random-uuid)
   :name           "Item"
   :quantity       1
   :order-position 0
   :category-id    category-id})

(def second-item (assoc first-item :order-position 1))
(def third-item (assoc first-item :order-position 2))

(def item-position-changed
  (assoc first-item :order-position 1))

(s/deftest change-order-position-test
  (testing "Should change the item order position"
    (is (= item-position-changed (logic.purchase-item/change-order-position 1 first-item))))
  (testing "Should set a default value when order position is not provided"
    (= first-item (logic.purchase-item/change-order-position nil item-position-changed))))

(s/deftest sort-by-position-test
  (testing "Should sort the items bu order position"
    (= [first-item second-item third-item]
       (logic.purchase-item/sort-by-position [second-item third-item first-item]))))

