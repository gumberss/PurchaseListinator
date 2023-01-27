(ns purchase-list.purchase-list-search-test
  (:require [clojure.test :refer :all]
            [state-flow.core :refer [flow]]
            [utils.integration-test :refer [integration-test]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [utils.http :as utils.http]))


(def list-id "5215075f-9a24-47e9-91fb-8485adb410f4")

(integration-test get-purchase-lists-test
  (flow "Inserting a new purchase-list"
    [{:keys [body] :as response} (utils.http/request! {:method   :post
                                                       :endpoint :post-purchases-lists
                                                       :body     {:name "List"}})]
    (match? {:status 200} response)
    (flow "Should be able to retrieve the existent purchase lists"
      [{:keys [body] :as response} (utils.http/request! {:method   :get
                                                         :endpoint :get-purchases-lists})]
      (match? {:status 200
               :body   [{:id          string?
                         :name        "List"
                         :enabled     true
                         :in-progress false}]} response))))

(integration-test purchases-lists-management-data-test
  (flow "Inserting a new purchase-list"
    [{:keys [body] :as response} (utils.http/request! {:method   :post
                                                       :endpoint :post-purchases-lists
                                                       :body     {:name "List"}})]
    (match? {:status 200} response)
    (flow "Should be able to retrieve the existent purchase lists"
      [{:keys [body] :as response} (utils.http/request! {:method       :get
                                                         :endpoint     :purchases-lists-management-data
                                                         :params {:id (str (:id body))}})]
      (match? {:status 200
               :body   {:id         (:id body)
                        :categories []}} response))))
