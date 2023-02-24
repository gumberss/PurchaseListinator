(ns purchase-listinator.adapters.in.shopping-cart-event-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.adapters.in.shopping-cart-event :as adapters.in.shopping-cart-event]
            [schema.test :as s]))

(def shipping-id "6412e718-72bf-4a29-8b3a-9c3fb532ab7b")
(def item-id "30010c9b-c70d-4268-8602-8214d9a75336")
(def event-id (str "99990c9b-c70d-4268-8602-8214d9a75336"))
(def user-id (random-uuid))
(def wire-reorder-category-event
  {:event-id     event-id
   :event-type   "reorder-category"
   :shopping-id  shipping-id
   :category-id  "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"
   :new-position 1})

(def wire-reorder-item-event
  {:event-id        event-id
   :event-type      "reorder-item"
   :shopping-id     shipping-id
   :item-id         "30010c9b-c70d-4268-8602-8214d9a75336"
   :new-position    1
   :new-category-id "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"})

(def wire-change-item-event
  {:event-id         event-id
   :event-type       "change-item"
   :shopping-id      shipping-id
   :item-id          item-id
   :price            12.0
   :quantity-changed 2})

(s/deftest wire->internal-test
  (testing "That we can internalize a shopping cart events from the wire"
    (is (= {:id           #uuid "99990c9b-c70d-4268-8602-8214d9a75336"
            :category-id  #uuid "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"
            :event-type   :reorder-category
            :user-id      user-id
            :moment       0
            :new-position 1
            :shopping-id  #uuid "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"}
           (adapters.in.shopping-cart-event/wire->internal wire-reorder-category-event 0 user-id)))
    (is (= {:id              #uuid "99990c9b-c70d-4268-8602-8214d9a75336"
            :event-type      :reorder-item
            :user-id         user-id
            :item-id         #uuid "30010c9b-c70d-4268-8602-8214d9a75336"
            :moment          0
            :new-category-id #uuid "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"
            :new-position    1
            :shopping-id     #uuid "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"}
           (adapters.in.shopping-cart-event/wire->internal wire-reorder-item-event 0 user-id)))
    (is (= {:id               #uuid "99990c9b-c70d-4268-8602-8214d9a75336"
            :event-type       :change-item
            :user-id          user-id
            :item-id          #uuid "30010c9b-c70d-4268-8602-8214d9a75336"
            :moment           0
            :price            12.0
            :quantity-changed 2
            :shopping-id      #uuid "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"}
           (adapters.in.shopping-cart-event/wire->internal wire-change-item-event 0 user-id)))))
