(ns purchase-listinator.logic.purchase-list-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test :refer [match?]]
            [purchase-listinator.logic.purchase-list :as logic.purchase-list]
            [schema.core :as s]))

(deftest generate-new-test
  (s/with-fn-validation
    (testing "Should generate a new default list with the correct named informed"
      (is (match? {:id          uuid?
                   :name        "Most beautiful purchase list"
                   :enabled     true
                   :in-progress false
                   :products    []}
                  (logic.purchase-list/generate-new "Most beautiful purchase list"))))))