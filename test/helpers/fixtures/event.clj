(ns fixtures.event
  (:require [clojure.test :refer :all]))

(def category-id (random-uuid))
(def purchase-list-id (random-uuid))

(def delete-category-event
  {:category-id      category-id
   :purchase-list-id purchase-list-id
   :moment           10})

(def create-category-event
  {:name             "random-name"
   :category-id      category-id
   :order-position   1
   :color            255
   :purchase-list-id purchase-list-id
   :moment           5})