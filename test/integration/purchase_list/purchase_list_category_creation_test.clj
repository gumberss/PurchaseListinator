(ns purchase-list.purchase-list-category-creation-test
  (:require [clojure.test :refer :all]
            [state-flow.core :refer [flow]]
            [utils.integration-test :refer [integration-test]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [utils.http :as utils.http]
            [purchase-listinator.components.rabbitmq :as components.rabbitmq]))

(def category-id "718060b1-53e5-4d80-9b25-a03f5f87119d")
(def list-id "5215075f-9a24-47e9-91fb-8485adb410f4")
(def user-id "be49fa29-c83c-4e55-970f-086690ba1984")
(integration-test purchase-list-category-creation-test
  (flow "Should create a new purchase list"
    [{:keys [body] :as response} (utils.http/request! {:method   :post
                                                       :endpoint :add-purchases-lists-category
                                                       :token    user-id
                                                       :body     {:name             "A category"
                                                                  :id               category-id
                                                                  :color            1321
                                                                  :purchase-list-id list-id}})]
    (match? {:status 200
             :body   {:name             "A category"
                      :id               category-id
                      :order-position   0
                      :user-id          user-id
                      :color            1321
                      :purchase-list-id list-id}} response)
    (match? {:event-id         uuid?
             :name             "A category"
             :category-id      (parse-uuid category-id)
             :user-id          (parse-uuid user-id)
             :purchase-list-id (parse-uuid list-id)
             :order-position   0
             :color            1321
             :moment           number?}
            (components.rabbitmq/first-event :purchase-listinator/purchase-list.category.created))))
