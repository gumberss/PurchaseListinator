(ns purchase-listinator.logic.purchase-category-test
  (:require [clojure.test :refer :all]
            [schema.test :as s]
            [purchase-listinator.logic.purchase-category :as logic.purchase-category]
            [purchase-listinator.misc.general :as misc.general]))

(def category-id (misc.general/squuid))

(def first-item
  {:id             (misc.general/squuid)
   :name           "Last"
   :quantity       1
   :order-position 0
   :category-id    category-id})

(def second-item (assoc first-item :order-position 1))
(def third-item (assoc first-item :order-position 2))

(def first-category
  {:name             "Category"
   :id               category-id
   :order-position   0
   :color            123
   :purchase-list-id (misc.general/squuid)
   :items            [third-item second-item first-item]})

(def second-category
  (assoc first-category :order-position 1))

(def third-category
  (assoc first-category :order-position 2))

(def category-with-items-ordered
  (assoc first-category :items [first-item second-item third-item]))

(s/deftest sort-items-by-position-test
  (testing "Should order category items by position"
    (is (= category-with-items-ordered (logic.purchase-category/sort-items-by-position first-category)))))

(s/deftest sort-by-position-test
  (testing "Should order catgories by position"
    (is (= [first-category second-category third-category]
           (logic.purchase-category/sort-by-position [third-category second-category first-category])))))
