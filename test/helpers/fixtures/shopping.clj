(ns fixtures.shopping
  (:require [clojure.test :refer :all]))

(def shopping-id (random-uuid))
(def list-id (random-uuid))

(def shopping-internal
  {:id         shopping-id
   :place      "random-place"
   :type       "random-type"
   :title      "random-title"
   :date       10
   :list-id    list-id
   :status     :in-progress
   :duration   20
   :categories []})
