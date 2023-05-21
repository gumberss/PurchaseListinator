(ns purchase-listinator.adapters.in.shopping-purchase-list-events-test
  (:require [clojure.test :refer :all]
            [fixtures.event]
            [purchase-listinator.adapters.in.shopping-purchase-list-events :as adapters.in.shopping-purchase-list-events]
            [schema.test :as s]))

(s/deftest category-deleted-event->internal-test
  (testing "That we can internalize a category deleted event from wire"
    (is (= {:id                           fixtures.event/event-id
            :moment                       30
            :event-type                   :purchase-list-category-deleted
            :user-id                      fixtures.event/user-id
            :category-id                  fixtures.event/category-id
            :purchase-list-id             fixtures.event/purchase-list-id}
           (adapters.in.shopping-purchase-list-events/category-deleted-event->internal fixtures.event/delete-category-event)))))

(s/deftest category-created-event->internal-test
  (testing "That we can internalize a category created event from wire"
    (is (= {:id               fixtures.event/event-id
            :moment           25
            :event-type       :purchase-list-category-created
            :user-id          fixtures.event/user-id
            :name             "random-category-name"
            :category-id      fixtures.event/category-id
            :order-position   3
            :color            255
            :purchase-list-id fixtures.event/purchase-list-id}
           (adapters.in.shopping-purchase-list-events/category-created-event->internal fixtures.event/create-category-event)))))

(s/deftest item-created-event->internal-test
  (testing "That we can internalize a item created event from wire"
    (is (= {:id                           fixtures.event/event-id
            :event-type                   :purchase-list-item-created
            :user-id                      fixtures.event/user-id
            :moment                       10
            :item-id                      fixtures.event/item-id
            :name                         "random-item-name"
            :quantity                     2
            :order-position               1
            :category-id                  fixtures.event/category-id}
           (adapters.in.shopping-purchase-list-events/item-created-event->internal fixtures.event/item-created-event-wire)))))

(s/deftest item-deleted-event->internal-test
  (testing "That we can internalize a item deleted event from wire"
    (is (= {:id                           fixtures.event/event-id
            :event-type                   :purchase-list-item-deleted
            :user-id                      fixtures.event/user-id
            :moment                       35
            :item-id                      fixtures.event/item-id
            :category-id                  fixtures.event/category-id}
           (adapters.in.shopping-purchase-list-events/item-deleted-event->internal fixtures.event/item-deleted-event-wire)))))

(s/deftest item-changed-event->internal-test
  (testing "That we can internalize a item changed event from wire"
    (is (= {:id             fixtures.event/event-id
            :event-type     :purchase-list-item-changed
            :user-id        fixtures.event/user-id
            :moment         40
            :item-id        fixtures.event/item-id
            :name           "random-item-name"
            :quantity       4
            :order-position 2
            :category-id    fixtures.event/category-id}
           (adapters.in.shopping-purchase-list-events/item-changed-event->internal fixtures.event/item-changed-event-wire)))))
