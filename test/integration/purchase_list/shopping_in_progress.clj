(ns purchase-list.shopping-in-progress
  (:require [clojure.test :refer :all]
            [purchase-listinator.wires.purchase-list.out.purchase-list :as wires.purchase-list.out.purchase-list]
            [state-flow.core :refer [flow]]
            [utils.integration-test :refer [integration-test]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [utils.http :as utils.http]))
(def category-id (random-uuid))
(def user-id (random-uuid))
(def item-id (random-uuid))
(integration-test shopping-in-progress-test
  (flow "Inserting a new purchase-list"
    [{:keys [body] :as response} (utils.http/request! {:method               :post
                                                       :endpoint             :post-purchases-lists
                                                       :body                 {:name "List"}
                                                       :token                user-id
                                                       :response-body-schema wires.purchase-list.out.purchase-list/PurchaseList})]
    (match? {:status 200} response)
    (let [list-id (:id body)]
      [{:keys [body] :as response} (utils.http/request! {:method   :post
                                                         :endpoint :add-purchases-lists-category
                                                         :token    user-id
                                                         :body     {:name             "A category"
                                                                    :id               (str category-id)
                                                                    :color            1321
                                                                    :purchase-list-id (:id body)}})]
      (match? {:status 200} response)

      [{:keys [body] :as response} (utils.http/request! {:method   :post
                                                         :endpoint :add-purchases-lists-item
                                                         :token    user-id
                                                         :body     {:name           "Item"
                                                                    :id             (str item-id)
                                                                    :quantity       10
                                                                    :order-position 0
                                                                    :category-id    (str category-id)}})]
      (match? {:status 200} response)
      )))


