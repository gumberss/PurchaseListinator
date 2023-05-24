(ns purchase-listinator.adapters.out.shopping-initiation-data-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.adapters.out.shopping-initiation-data :as adapters.out.shopping-initiation-data]
            [schema.test :as s]
            [fixtures.shopping]))

(s/deftest shopping->wire-test
  (testing "That we can externalize a shopping entity"
    (is (= {:place "random-place"
            :type  "random-type"}
           (adapters.out.shopping-initiation-data/shopping->wire fixtures.shopping/shopping-internal)))))
