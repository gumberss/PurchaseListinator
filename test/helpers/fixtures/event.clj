(ns fixtures.event
  (:require [clojure.test :refer :all]))

(def event-id (random-uuid))
(def user-id (random-uuid))
(def item-id (random-uuid))
(def category-id (random-uuid))
(def purchase-list-id (random-uuid))
(def shopping-id (random-uuid))

(def delete-category-event
  {:event-id         event-id
   :category-id      category-id
   :purchase-list-id purchase-list-id
   :user-id          user-id
   :moment           30})

(def create-category-event
  {:event-id         event-id
   :user-id          user-id
   :name             "random-category-name"
   :category-id      category-id
   :order-position   3
   :color            255
   :purchase-list-id purchase-list-id
   :moment           25})

(def item-created-event-wire
  {:event-id       event-id
   :moment         10
   :item-id        item-id
   :name           "random-item-name"
   :quantity       2
   :order-position 1
   :category-id    category-id
   :user-id        user-id})

(def item-deleted-event-wire
  {:event-id    event-id
   :item-id     item-id
   :category-id category-id
   :moment      35
   :user-id     user-id})

(def item-changed-event-wire
  {:event-id       event-id
   :moment         40
   :item-id        item-id
   :name           "random-item-name"
   :quantity       4
   :order-position 2
   :category-id    category-id
   :user-id        user-id})
