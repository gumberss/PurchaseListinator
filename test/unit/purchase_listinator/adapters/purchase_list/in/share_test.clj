(ns purchase-listinator.adapters.purchase-list.in.share-test
  (:require [clojure.test :refer :all]
            [schema.test :as st]
            [purchase-listinator.adapters.purchase-list.in.share :as adapters.purchase-list.in.share]))

(def list-id (random-uuid))
(def share-list-wire
  {:list-id           (str list-id)
   :customer-nickname "Custom Nickname"})

(def share-list-internal
  {:list-id           (str list-id)
   :customer-nickname "Custom Nickname"})

(st/deftest wire->internal-test
  (testing "Adapting share list"
    (is (= share-list-internal
           (adapters.purchase-list.in.share/wire->internal share-list-wire)))))