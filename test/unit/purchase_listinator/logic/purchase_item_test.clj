(ns purchase-listinator.logic.purchase-item-test
  (:require [clojure.test :refer :all]
            [schema.test :as s]
            [purchase-listinator.logic.purchase-item :as logic.purchase-item]))

(def category-id (random-uuid))
(def user-id (random-uuid))

(def first-item
  {:id             (random-uuid)
   :name           "Item"
   :user-id user-id
   :quantity       1
   :order-position 0
   :category-id    category-id})

(def second-item
  (assoc first-item :order-position 1
                    :id (random-uuid)))
(def third-item
  (assoc first-item :order-position 2
                    :id (random-uuid)))

(def shopping-item
  {:id               (:id first-item)
   :quantity-in-cart 10})

(def item-position-changed
  (assoc first-item :order-position 1))

(s/deftest change-order-position-test
  (testing "Should change the item order position"
    (is (= item-position-changed (logic.purchase-item/change-order-position 1 first-item))))
  (testing "Should set a default value when order position is not provided"
    (= first-item (logic.purchase-item/change-order-position nil item-position-changed))))

(s/deftest sort-by-position-test
  (testing "Should sort the items bu order position"
    (is (= [first-item second-item third-item]
           (logic.purchase-item/sort-by-position [second-item third-item first-item])))))

(s/deftest update-quantity-test
  (testing "Should update the item quantity"
    (is (= -9 (-> (logic.purchase-item/update-quantity-by-shopping-item first-item shopping-item)
                  :quantity)))))

(s/deftest find-by-shopping-item-test
  (testing "Should find the item by the shopping item"
    (is (= first-item (logic.purchase-item/find-by-shopping-item [third-item second-item first-item] shopping-item)))))

(s/deftest find-item-and-update-test
  (testing "Should find and update the item by shopping"
    (is (= (assoc first-item :quantity -9)
           (logic.purchase-item/find-item-and-update-item-quantity [third-item second-item first-item] shopping-item)))))

(def new-first-item
  (assoc first-item :quantity 5))

(def new-second-item
  (assoc second-item :quantity 3))

(def new-third-item
  (assoc third-item :quantity 2))

(def old-items
  [first-item second-item third-item])

(def new-items
  [new-first-item new-second-item new-third-item])

(s/deftest build-items-pair-test
  (testing "Should build the item pairs"
    (is (= [[first-item new-first-item]
            [second-item new-second-item]
            [third-item new-third-item]]
           (logic.purchase-item/build-items-pair old-items new-items)))))



