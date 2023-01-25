(ns utils.http
  (:require [io.pedestal.http :as http]
            [purchase-listinator.components.pedestal :as components.pedestal]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :refer [response-for]]
            [clojure.test :refer :all]
            [state-flow.api :as state-flow.api]
            [state-flow.core :refer [flow]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [state-flow.api :as flow]
            [utils.integration-test :refer [integration-test]]))

(defn url-for
  ([url]
   (url-for url nil))
  ([url params]
   (url-for url params {"Content-Type" "application/json"}))
  ([url params headers]
   (let [url-for (route/url-for-routes (route/expand-routes components.pedestal/all-routes))]
     (url-for url :headers headers
              :params params))))

(defn service-fn
  [pedestal]
  (utils.integration-test/get-component pedestal [:service ::http/service-fn]))

(defn request!
  [{:keys [method endpoint params]}]
  (flow (str "Requesting -" method " - " endpoint)
    [service (state-flow.api/get-state :pedestal)]
    (let [service (service-fn service)
          outcome (response-for service method (url-for endpoint params))]

      (flow/return outcome))))

(integration-test first-test
  (flow "first test"
    (let [{:keys [status body] :as a} (request! {:method   :get
                                                 :endpoint :api-version
                                                 :params   {:id (str (random-uuid))}})]

      (match? {:status 200} a))))
