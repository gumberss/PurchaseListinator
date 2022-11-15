(ns purchase-listinator.adapters.in.shopping-initiation-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.adapters.in.shopping-initiation :as adapters.in.shopping-initiation]
            [schema.test :as s]))

(def wire-shopping-initiation
  {:id        "685c8032-793d-456f-9d6a-fbc8dcb21abb"
   :place     "random place"
   :type      "random type"
   :title     "random title"
   :list-id   "55362b9c-09de-4447-b3cd-92c10a85300e"
   :latitude  10
   :longitude 0})

(s/deftest wire->internal-test
  (testing "That we can internalize a shopping initiation from the wire"
    (is (= {:id        #uuid "685c8032-793d-456f-9d6a-fbc8dcb21abb"
            :latitude  10
            :list-id   #uuid "55362b9c-09de-4447-b3cd-92c10a85300e"
            :longitude 0
            :place     "random place"
            :title     "random title"
            :type      "random type"}
           (adapters.in.shopping-initiation/wire->internal wire-shopping-initiation)))))
