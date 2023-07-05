(ns purchase-listinator.modules.shopping-cart.adapters.in.purchase-list-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.modules.shopping-cart.adapters.in.purchase-list :as modules.shopping-cart.adapters.in.purchase-list]
            [schema.test :as st]))

(def user-id (random-uuid))
(def purchase-list-id (random-uuid))

(def wire-purchase-item
  {:name                  "item"
   :id                    #uuid "c99f7f6a-6740-43e1-ae9b-a765d8b89d92"
   :quantity              1
   :order-position        0
   :category-id           #uuid "b3448f5c-6b37-498c-859a-cf5574b01108"
   :random-loose-property :lala})
(def internal-item (dissoc wire-purchase-item :random-loose-property))
(def wire-purchase-category
  {:name                  "Category"
   :id                    #uuid "b3448f5c-6b37-498c-859a-cf5574b01108"
   :order-position        0
   :color                 0
   :purchase-list-id      #uuid "1defd2b6-7abe-4869-8da4-fe73209c849c"
   :items                 [wire-purchase-item]
   :random-loose-property 123})
(def internal-category
  (-> (dissoc wire-purchase-category :random-loose-property)
      (assoc :items [internal-item])))

(def wire-purchase-list
  {:id                    purchase-list-id
   :categories            [wire-purchase-category]
   :random-loose-property "lala"})

(def internal-purchase-list
  (-> (dissoc wire-purchase-list :random-loose-property)
      (assoc :categories [internal-category])))

(st/deftest wire-item->internal-test
  (testing "Item adapter"
    (is (= internal-item
           (modules.shopping-cart.adapters.in.purchase-list/wire-items->internal wire-purchase-item)))))

(st/deftest wire-category->internal-test
  (testing "category adapter"
    (is (= internal-category
           (modules.shopping-cart.adapters.in.purchase-list/category-wire->internal wire-purchase-category)))))

(st/deftest wire-purchase-list->internal-test
  (testing "purchase-list adapter"
    (is (= internal-purchase-list
           (modules.shopping-cart.adapters.in.purchase-list/purchase-list-wire->internal wire-purchase-list)))))