(ns purchase-listinator.modules.shopping.logic.shopping-cart-event-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.modules.shopping.logic.shopping-cart-event :as logic.shopping-cart-event]
            [schema.test :as st]))

(def purchase-list-id (random-uuid))
(def shopping-id (random-uuid))
(def category-id (random-uuid))
(def category2-id (random-uuid))
(def item-id (random-uuid))
(def user-id (random-uuid))

(def item1
  {:id               item-id
   :name             "item"
   :user-id          user-id
   :quantity         15
   :price            0
   :quantity-in-cart 0
   :order-position   0
   :category-id      category-id})

(def category1
  {:name             "Category 1"
   :id               category-id
   :user-id          user-id
   :order-position   1
   :color            123
   :purchase-list-id purchase-list-id
   :items            [item1]})

(def category2
  (assoc category1 :id category2-id
                   :name "Category 2"
                   :items []
                   :order-position 2))

(def shopping-list
  {:purchase-list-id purchase-list-id
   :user-id          user-id
   :shopping-id      shopping-id
   :categories       [category1 category2]})

(def change-item-event
  {:id               (random-uuid)
   :user-id          user-id
   :moment           123
   :event-type       :change-item
   :shopping-id      shopping-id
   :purchase-list-id purchase-list-id
   :item-id          item-id
   :price            0
   :quantity-changed 10})

(def change-item-removing-event
  {:id               (random-uuid)
   :user-id          user-id
   :moment           123
   :event-type       :change-item
   :purchase-list-id purchase-list-id
   :shopping-id      shopping-id
   :item-id          item-id
   :price            0
   :quantity-changed -5})

(st/deftest apply-event-change-item-test
  (testing "Should change an item in the cart"
    (testing "When adding items in the cart"
      (is (= (assoc-in shopping-list [:categories 0 :items 0 :quantity-in-cart] 10)
             (logic.shopping-cart-event/apply-event change-item-event shopping-list))))
    (testing "When removing items in the cart"
      (let [shopping (assoc-in shopping-list [:categories 0 :items 0 :quantity-in-cart] 5)
            expected-shopping (assoc-in shopping-list [:categories 0 :items 0 :quantity-in-cart] 0)]
        (is (= expected-shopping
               (logic.shopping-cart-event/apply-event change-item-removing-event shopping)))))
    (testing "When adding more items in the cart"
      (let [shopping (assoc-in shopping-list [:categories 0 :items 0 :quantity-in-cart] 5)
            expected-shopping (assoc-in shopping-list [:categories 0 :items 0 :quantity-in-cart] 15)]
        (is (= expected-shopping
               (logic.shopping-cart-event/apply-event change-item-event shopping)))))
    (testing "When changing the price of the item"
      (let [expected-shopping (update-in shopping-list [:categories 0 :items 0] assoc :price 50.30 :quantity-in-cart 10)
            event (assoc change-item-event :price 50.30)]
        (is (= expected-shopping
               (logic.shopping-cart-event/apply-event event shopping-list)))))))

(def new-category-id (random-uuid))
(def purchase-list-category-created-event
  {:id               (random-uuid)
   :moment           123
   :event-type       :purchase-list-category-created
   :name             "New category"
   :user-id          user-id
   :category-id      new-category-id
   :order-position   50
   :color            321
   :purchase-list-id purchase-list-id})

(st/deftest apply-event-purchase-list-category-created-test
  (testing "Should add a category in the shopping when processing a category created event"
    (let [{:keys [categories]} (logic.shopping-cart-event/apply-event purchase-list-category-created-event shopping-list)]
      (is (= 3 (count categories)))
      (is (= {:name             "New category"
              :id               new-category-id
              :user-id          user-id
              :order-position   50
              :color            321
              :purchase-list-id purchase-list-id
              :items            []} (last categories))))))

(def purchase-list-category-deleted-event
  {:id               (random-uuid)
   :moment           213
   :event-type       :purchase-list-category-deleted
   :user-id          user-id
   :category-id      category-id
   :purchase-list-id purchase-list-id})

(st/deftest apply-event-purchase-list-category-deleted-test
  (testing "Should remove the category in the shopping when processing a category deleted event"
    (let [{:keys [categories]} (logic.shopping-cart-event/apply-event purchase-list-category-deleted-event shopping-list)]
      (is (= 1 (count categories))))))
