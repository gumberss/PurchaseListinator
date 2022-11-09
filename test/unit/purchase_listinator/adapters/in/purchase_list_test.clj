(ns purchase-listinator.adapters.in.purchase-list-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.adapters.in.purchase-list :as adapters.in.purchase-list]
            [schema.test :as s]))

(def wire-purchase-list
  {:name        "random name"
   :id          "f948399f-29c3-402e-81ed-b0d695fc792a"
   :enabled     true
   :in-progress true
   :status      :in-progress})

(s/deftest wire->internal-test
  (testing "That we can internalize a purchase list from the wire"
    (is (= {:enabled     true
            :id          #uuid "f948399f-29c3-402e-81ed-b0d695fc792a"
            :in-progress true
            :name        "random name"
            :status      :in-progress}
           (adapters.in.purchase-list/wire->internal wire-purchase-list)))))
