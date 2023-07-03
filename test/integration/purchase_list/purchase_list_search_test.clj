(ns purchase-list.purchase-list-search-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.misc.date :as misc.date]
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
      [{:keys [body] :as response} (utils.http/request! {:method      :get
                                                         :endpoint    :purchases-lists-management-data
                                                         :path-params {:id (str (:id body))}})]
      (match? {:status 200
               :body   {:id         (:id body)
                        :categories []}} response))))

(integration-test get-purchases-lists-test
  (flow "Inserting a new purchase-list"
    [{:keys [body] :as response} (utils.http/request! {:method   :post
                                                       :endpoint :post-purchases-lists
                                                       :body     {:name "List"}})]
    (match? {:status 200} response)
    (flow "Should be able to retrieve the existent list"
      [{:keys [body] :as response} (utils.http/request! {:method      :get
                                                         :endpoint    :get-list-default
                                                         :path-params {:id (str (:id body))}
                                                         :body        {:moment (misc.date/numb-now)}})]
      (match? {:status 200
               :body   {:id         (:id body)
                        :categories []}} response))))

(integration-test get-purchases-lists-no-date-sent-test
  (flow "Inserting a new purchase-list"
    [{:keys [body] :as response} (utils.http/request! {:method   :post
                                                       :endpoint :post-purchases-lists
                                                       :body     {:name "List"}})]
    (match? {:status 200} response)
    (flow "Should be able to retrieve the existent list"
      [{:keys [body] :as response} (utils.http/request! {:method      :get
                                                         :endpoint    :get-list-default
                                                         :path-params {:id (str (:id body))}})]
      (match? {:status 200
               :body   {:id         (:id body)
                        :categories []}} response))))

(integration-test get-purchases-lists-not-found-test
  (flow "Should not be able to retrieve the existent list when the list does not exist"
    [response (utils.http/request! {:method      :get
                                    :endpoint    :get-list-default
                                    :path-params {:id (str (random-uuid))}
                                    :body        {:moment (misc.date/numb-now)}})]
    (match? {:status 404
             :body   {:message "[[LIST_NOT_FOUND]]"}} response)))

(integration-test purchases-list-request-date-before-creation-test
  (let [before-the-creation-time (misc.date/numb-now)]
    (flow "Inserting a new purchase-list"

      [{:keys [body] :as response} (utils.http/request! {:method   :post
                                                         :endpoint :post-purchases-lists
                                                         :body     {:name "List"}})]
      (match? {:status 200} response)
      (flow "Should not be able to retrieve the existent list when the request moment date is before the list creation date"
        [response (utils.http/request! {:method      :get
                                        :endpoint    :get-list-default
                                        :path-params {:id (str (:id body))}
                                        :body        {:moment before-the-creation-time}})]
        (match? {:status 404
                 :body   {:message "[[LIST_NOT_FOUND]]"}} response)))))