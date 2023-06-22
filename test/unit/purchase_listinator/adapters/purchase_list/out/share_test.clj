(ns purchase-listinator.adapters.purchase-list.out.share-test
  (:require [clojure.test :refer :all]
            [schema.test :as st]
            [purchase-listinator.adapters.purchase-list.out.share :as adapters.purchase-list.out.share]))

(def list-id (random-uuid))
(def customer-id (random-uuid))
(def share-list-internal
  {:list-id     list-id
   :customer-id customer-id})

(def share-list-db
  {:purchase-list-share/list-id     list-id
   :purchase-list-share/customer-id customer-id})

(st/deftest wire->internal-test
  (testing "Adapting share list"
    (is (= share-list-db
           (adapters.purchase-list.out.share/internal->db share-list-internal)))))