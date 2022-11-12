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
