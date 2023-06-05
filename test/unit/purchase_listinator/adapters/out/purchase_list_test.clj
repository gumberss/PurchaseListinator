(ns purchase-listinator.adapters.out.purchase-list-test
  (:require [clojure.test :refer :all]
            [fixtures.purchase-list]
            [purchase-listinator.adapters.out.purchase-list :as adapters.out.purchase-list]
            [schema.test :as s]))

(s/deftest internal->wire-test
  (testing "That we can externalise a internal purchase list entity to wire"
    (is (= {:enabled     true
            :id          (str fixtures.purchase-list/purchase-list-id)
            :in-progress false
            :name        "random purchase list name"
            :user-id     (str fixtures.purchase-list/user-id)}
           (adapters.out.purchase-list/internal->wire fixtures.purchase-list/purchase-list-internal)))))
