(ns purchase-listinator.adapters.in.shopping-cart-event-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.adapters.in.shopping-cart-event :as adapters.in.shopping-cart-event]
            [schema.test :as s]))

(def shipping-id "6412e718-72bf-4a29-8b3a-9c3fb532ab7b")
(def item-id "30010c9b-c70d-4268-8602-8214d9a75336")

(def wire-reorder-category-event
  {:event-type   "reorder-category"
   :shopping-id  shipping-id
   :category-id  "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"
   :old-position 0
   :new-position 1})

(def wire-reorder-item-event
  {:event-type      "reorder-item"
   :shopping-id     shipping-id
   :item-id         "30010c9b-c70d-4268-8602-8214d9a75336"
   :old-position    0
   :new-position    1
   :new-category-id "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"})

(def wire-change-item-event
  {:event-type       "change-item"
   :shopping-id      shipping-id
   :item-id          item-id
   :price            12.0
   :quantity-changed 2})

(s/deftest wire->internal-test
  (testing "That we can internalize a shopping cart events from the wire"
    (is (= {:category-id  #uuid "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"
            :event-type   :reorder-category
            :moment       0
            :new-position 1
            :old-position 0
            :shopping-id  #uuid "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"}
           (adapters.in.shopping-cart-event/wire->internal wire-reorder-category-event 0)))
    (is (= {:event-type      :reorder-item
            :item-id         #uuid "30010c9b-c70d-4268-8602-8214d9a75336"
            :moment          0
            :new-category-id #uuid "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"
            :new-position    1
            :old-position    0
            :shopping-id     #uuid "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"}
           (adapters.in.shopping-cart-event/wire->internal wire-reorder-item-event 0)))
    (is (= {:event-type       :change-item
            :item-id          #uuid "30010c9b-c70d-4268-8602-8214d9a75336"
            :moment           0
            :price            12.0
            :quantity-changed 2
            :shopping-id      #uuid "6412e718-72bf-4a29-8b3a-9c3fb532ab7b"}
           (adapters.in.shopping-cart-event/wire->internal wire-change-item-event 0)))))
