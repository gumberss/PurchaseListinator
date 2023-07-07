(ns purchase-list.purchase-list-item-creation-test
  (:require [clojure.test :refer :all]
            [state-flow.core :refer [flow]]
            [utils.integration-test :refer [integration-test]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [utils.http :as utils.http]))



(def category-id "718060b1-53e5-4d80-9b25-a03f5f87119d")
(def list-id "5215075f-9a24-47e9-91fb-8485adb410f4")
(def user-id "be49fa29-c83c-4e55-970f-086690ba1984")
(def item-id (str (random-uuid)))
(integration-test purchase-list-item-creation-test
  (flow "Should create a new purchase item"
    [list-response (utils.http/request! {:method   :post
                                         :endpoint :post-purchases-lists
                                         :token    user-id
                                         :body     {:name "List 3"}})]


    [category-response (utils.http/request! {:method   :post
                                             :endpoint :add-purchases-lists-category
                                             :token    user-id
                                             :body     {:name             "A category"
                                                        :id               category-id
                                                        :color            1321
                                                        :purchase-list-id (-> list-response :body :id)}})]

    [item-response (utils.http/request! {:method   :post
                                         :endpoint :add-purchases-lists-item
                                         :token    user-id
                                         :body     {:name           "Item"
                                                    :id             item-id
                                                    :quantity       10
                                                    :order-position 0
                                                    :category-id    (-> category-response :body :id)}})]
    (match? {:status 200
             :body   {:name           "Item"
                      :id             item-id
                      :quantity       10
                      :order-position 0
                      :user-id        user-id
                      :category-id    (-> category-response :body :id)}} item-response)))
