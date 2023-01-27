(ns purchase-list.purchase-list-category-creation-test
  (:require [clojure.test :refer :all]
            [state-flow.core :refer [flow]]
            [utils.integration-test :refer [integration-test]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [utils.http :as utils.http]
            [purchase-listinator.dbs.datomic.purchase-list :as dbs.datomic.purchase-list]))

(def category-id "718060b1-53e5-4d80-9b25-a03f5f87119d")
(def list-id "5215075f-9a24-47e9-91fb-8485adb410f4")

(integration-test purchase-list-category-creation-test
  (flow "Should create a new purchase list"
    [{:keys [body] :as response} (utils.http/request! {:method   :post
                                                       :endpoint :add-purchases-lists-category
                                                       :body      {:name             "A category"
                                                                   :id               category-id
                                                                   :color            1321
                                                                   :purchase-list-id list-id}})]
    (match? {:status 200
             :body   {:name             "A category"
                      :id               category-id
                      :order-position   0
                      :color            1321
                      :purchase-list-id list-id}} response)))
