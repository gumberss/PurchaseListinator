(ns fixtures.purchase-list
  (:require [clojure.test :refer :all]))

(def purchase-list-id (random-uuid))
(def user-id (random-uuid))

(def purchase-list-internal
  {:id          purchase-list-id
   :name        "random purchase list name"
   :user-id     user-id
   :enabled     true
   :in-progress false})
