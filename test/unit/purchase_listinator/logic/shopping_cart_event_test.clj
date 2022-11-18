(ns purchase-listinator.logic.shopping-cart-event-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.logic.shopping-cart-event :as logic.shopping-cart-event]
            [schema.test :as st]))

(def purchase-list-id (random-uuid))
(def shopping-id (random-uuid))
(def category-id (random-uuid))
(def item-id (random-uuid))

(def item1
  {:id               item-id
   :name             "item"
   :quantity         15
   :price            0
   :quantity-in-cart 0
   :order-position   0
   :category-id      category-id})

(def category1
  {:name             "Category 1"
   :id               category-id
   :order-position   1
   :color            123
   :purchase-list-id purchase-list-id
   :items            [item1]})

(def shopping-list
  {:purchase-list-id purchase-list-id
   :shopping-id      shopping-id
   :categories       [category1]})

(def change-item-event
  {:moment           123
   :event-type       :change-item
   :shopping-id      shopping-id
   :item-id          item-id
   :price            0
   :quantity-changed 10})

(def change-item-removing-event
  {:moment           123
   :event-type       :change-item
   :shopping-id      shopping-id
   :item-id          item-id
   :price            0
   :quantity-changed -5})

(def expected-item1
  {:id               item-id
   :name             "item"
   :quantity         15
   :price            0
   :quantity-in-cart 10
   :order-position   0
   :category-id      category-id})

(def expected-category1
  {:name             "Category 1"
   :id               category-id
   :order-position   1
   :color            123
   :purchase-list-id purchase-list-id
   :items            [expected-item1]})

(def expected-shopping-list
  {:purchase-list-id purchase-list-id
   :shopping-id      shopping-id
   :categories       [expected-category1]})

(st/deftest apply-event-change-item-test
  (testing "Should change an item in the cart"
    (testing "When adding items in the cart"
      (is (= expected-shopping-list
             (logic.shopping-cart-event/apply-event change-item-event shopping-list))))
    (testing "When removing items in the cart"
      (let [shopping (assoc-in shopping-list [:categories 0 :items 0 :quantity-in-cart] 5)
            expected-shopping (assoc-in expected-shopping-list [:categories 0 :items 0 :quantity-in-cart] 0)]
        (is (= expected-shopping
               (logic.shopping-cart-event/apply-event change-item-removing-event shopping)))))
    (testing "When adding more items in the cart"
      (let [shopping (assoc-in shopping-list [:categories 0 :items 0 :quantity-in-cart] 5)
            expected-shopping (assoc-in expected-shopping-list [:categories 0 :items 0 :quantity-in-cart] 15)]
        (is (= expected-shopping
               (logic.shopping-cart-event/apply-event change-item-event shopping)))))
    (testing "When changing the price of the item"
      (let [expected-shopping (assoc-in expected-shopping-list [:categories 0 :items 0 :price] 50.30)
            event (assoc change-item-event :price 50.30)]
        (is (= expected-shopping
               (logic.shopping-cart-event/apply-event event shopping-list)))))))

(def new-category-id (random-uuid))
(def purchase-list-category-created-event
  {:moment           123
   :event-type       :purchase-list-category-created
   :name             "New category"
   :category-id      new-category-id
   :order-position   50
   :color            321
   :purchase-list-id purchase-list-id})

(st/deftest apply-event-purchase-list-category-created-test
  (testing "Should add a category in the shopping when processing a category created event"
    (let [{:keys [categories]} (logic.shopping-cart-event/apply-event purchase-list-category-created-event shopping-list)]
      (is (= 2 (count categories)))
      (is (= {:name             "New category"
              :id               new-category-id
              :order-position   50
              :color            321
              :purchase-list-id purchase-list-id
              :items            []} (last categories))))))
