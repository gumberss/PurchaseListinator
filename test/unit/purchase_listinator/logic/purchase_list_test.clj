(ns purchase-listinator.logic.purchase-list-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test :refer [match?]]
            [purchase-listinator.logic.purchase-list :as logic.purchase-list]
            [schema.test :as s]))

(def user-id (random-uuid))

(s/deftest generate-new-test
  (testing "Should generate a new default list with the correct named informed"
    (is (match? {:id          uuid?
                 :name        "Most beautiful purchase list"
                 :enabled     true
                 :in-progress false}
                (logic.purchase-list/generate-new "Most beautiful purchase list" user-id)))))
(def purchase-list
  {:id          (random-uuid)
   :name        "Most beautiful purchase list"
   :user-id     user-id
   :enabled     true
   :in-progress false})

(def other-list
  {:id          (random-uuid)
   :name        "other-name"
   :user-id     user-id
   :enabled     true
   :in-progress false})

(s/deftest changed?-test
  (testing "Should return that is changed when the name is different"
    (is (true? (logic.purchase-list/changed? purchase-list other-list))))
  (testing "Should return that is not changed when are the same name"
    (is (false? (logic.purchase-list/changed? purchase-list (assoc other-list :name "Most beautiful purchase list"))))))
