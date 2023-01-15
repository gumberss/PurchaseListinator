(ns purchase-listinator.adapters.purchase-list.in.purchase-category-test
  (:require [clojure.test :refer :all]
            [schema.test :as s]
            [purchase-listinator.adapters.purchase-list.in.purchase-category :as adapters.in.purchase-category]))

(def wire-purchase-category
  {:name             "Lorem ipsum dolor sit amet"
   :id               "3e8b9081-ba97-48a6-8b33-3bf6ebf8ad5a"
   :order-position   0
   :color            0
   :purchase-list-id "1defd2b6-7abe-4869-8da4-fe73209c849c"})

(s/deftest wire->internal-test
  (testing "That we can internalize a wire purchase category"
    (is (= {:color            0
            :id               #uuid "3e8b9081-ba97-48a6-8b33-3bf6ebf8ad5a"
            :name             "Lorem ipsum dolor sit amet"
            :order-position   0
            :purchase-list-id #uuid "1defd2b6-7abe-4869-8da4-fe73209c849c"}
           (adapters.in.purchase-category/wire->internal wire-purchase-category)))))
