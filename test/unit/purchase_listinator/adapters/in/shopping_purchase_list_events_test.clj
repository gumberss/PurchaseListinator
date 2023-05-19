(ns purchase-listinator.adapters.in.shopping-purchase-list-events-test
  (:require [clojure.test :refer :all]
            [fixtures.event]
            [purchase-listinator.adapters.in.shopping-purchase-list-events :as adapters.in.shopping-purchase-list-events]
            [schema.test :as s]))

(s/deftest category-deleted-event->internal-test
  (testing "That we can internalize a category deleted event from wire"
    (is (= {:category-id      fixtures.event/category-id
            :event-type       :purchase-list-category-deleted
            :moment           10
            :purchase-list-id fixtures.event/purchase-list-id}
           (adapters.in.shopping-purchase-list-events/category-deleted-event->internal fixtures.event/delete-category-event)))))

(s/deftest category-created-event->internal-test
  (testing "That we can internalize a category created event from wire"
    (is (= {:category-id      fixtures.event/category-id
            :color            255
            :event-type       :purchase-list-category-created
            :moment           5
            :name             "random-name"
            :order-position   1
            :purchase-list-id fixtures.event/purchase-list-id}
           (adapters.in.shopping-purchase-list-events/category-created-event->internal fixtures.event/create-category-event)))))
