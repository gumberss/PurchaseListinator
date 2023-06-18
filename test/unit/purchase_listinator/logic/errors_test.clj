(ns purchase-listinator.logic.errors-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.logic.errors :as logic.errors]
            [schema.test :as s]))

(s/deftest build-test
  (testing "Should bould the error with status"
    (is (= {:status 500 :error {:message "Server error"}} (logic.errors/build 500 {:message "Server error"})))))
