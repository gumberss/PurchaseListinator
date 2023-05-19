(ns purchase-listinator.adapters.purchase-list.in.purchase-list-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.adapters.purchase-list.in.purchase-list :as adapters.in.purchase-list]
            [schema.test :as s]))

(def user-id (random-uuid))

(def wire-purchase-list
  {:name        "random name"
   :id          "f948399f-29c3-402e-81ed-b0d695fc792a"
   :user-id     user-id
   :enabled     true
   :in-progress true})

(s/deftest wire->internal-test
  (testing "That we can internalize a purchase list from the wire"
    (is (= {:enabled     true
            :id          #uuid "f948399f-29c3-402e-81ed-b0d695fc792a"
            :user-id     user-id
            :in-progress true
            :name        "random name"}
           (adapters.in.purchase-list/wire->internal wire-purchase-list)))))
