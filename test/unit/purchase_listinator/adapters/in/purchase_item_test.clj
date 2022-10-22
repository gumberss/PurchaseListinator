(ns purchase-listinator.adapters.in.purchase-item-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.adapters.in.purchase-item :as adapters.in.purchase-item]
            [schema.test :as s]))

(def wire-purchase-item
  {:name           "random purchase item"
   :id             "33c793c2-13ac-43c1-86af-dee398ae79b6"
   :quantity       1
   :order-position 0
   :category-id    "1d2dcffc-9ad5-415f-9d5c-0b8c40cddcf4"})

(s/deftest wire->internal-test
  (testing "That we can internalize a wire purchase item"
    (is (= {:category-id    #uuid "1d2dcffc-9ad5-415f-9d5c-0b8c40cddcf4"
            :id             #uuid "33c793c2-13ac-43c1-86af-dee398ae79b6"
            :name           "random purchase item"
            :order-position 0
            :quantity       1}
           (adapters.in.purchase-item/wire->internal wire-purchase-item)))))
