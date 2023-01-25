(ns purchase-list.purchase-list-creation-test
  (:require [clojure.test :refer :all]
            [state-flow.core :refer [flow]]
            [utils.integration-test :refer [integration-test]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [utils.http :as utils.http]))


(def list-id "5215075f-9a24-47e9-91fb-8485adb410f4")

(integration-test purchase-list-creation
  (flow "Post purchase list"
    (match? {:status 200} (utils.http/request! {:method   :post
                                  :endpoint :post-purchases-lists
                                  :body     {:name        "List 3"
                                             :id          list-id
                                             :enabled     true
                                             :in-progress false}}))))
