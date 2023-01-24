(ns purchase-listinator.purchase-listinator.purchase-list.purchase-list-creation-flow-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as client]
            [state-flow.api :as api :refer [flow match?]]))

(defn request [req]
  (flow "make request"
        [http (api/get-state :service)]
        (api/return (client/request http req))))

(api/defflow users
              (flow "fetch registered users"
                    (request {:method :post
                              :uri    "/users"
                              :body   {:user/first-name "David"}})
                    [users (request {:method :get
                                     :uri    "/users"})]
                    (match? ["David"]
                            (map :user/first-name users))))



