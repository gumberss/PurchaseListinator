(ns purchase-listinator.adapters.in.shopping-initiation-data-request-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.adapters.in.shopping-initiation-data-request :as adapters.in.shopping-initiation-data-request]
            [schema.test :as s]))

(def wire-shopping-initiation-data-request
  {:list-id   "70130cd0-ffe2-49f6-8707-7590dcdb8c9b"
   :latitude  "1.0"
   :longitude "2.0"})

(s/deftest wire->internal-test
  (testing "That we can internalize a shopping initiation data request from wire"
    (is (= {:list-id   #uuid "70130cd0-ffe2-49f6-8707-7590dcdb8c9b"
            :latitude  1.0
            :longitude 2.0}
           (adapters.in.shopping-initiation-data-request/wire->internal wire-shopping-initiation-data-request)))))
