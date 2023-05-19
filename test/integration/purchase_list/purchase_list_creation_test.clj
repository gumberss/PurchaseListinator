(ns purchase-list.purchase-list-creation-test
  (:require [clojure.test :refer :all]
            [state-flow.core :refer [flow]]
            [utils.integration-test :refer [integration-test]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [utils.http :as utils.http]
            [purchase-listinator.dbs.datomic.purchase-list :as dbs.datomic.purchase-list]))

(def list-id "5215075f-9a24-47e9-91fb-8485adb410f4")
(def user-id (random-uuid))
(integration-test purchase-list-creation-test
  (flow "Should create a new purchase list"
        [{:keys [body] :as response} (utils.http/request! {:method   :post
                                                           :endpoint :post-purchases-lists
                                                           :token    user-id
                                                           :body     {:name "List 3"}})]
        (match? {:status 200
                 :body   {:id          string?
                          :name        "List 3"
                          :enabled     true
                          :in-progress false}} response)
        (flow "Should have a purchase list inserted"
              [datomic (state-flow.api/get-state :datomic)]
              (match? {:id (parse-uuid (:id body)) :name "List 3"}
                      (dbs.datomic.purchase-list/get-by-name "List 3" user-id datomic)))))
