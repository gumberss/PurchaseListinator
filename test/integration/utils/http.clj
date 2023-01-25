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
            [utils.integration-test :refer [integration-test]]
            [purchase-listinator.misc.content-type-parser :as misc.content-type-parser]))

(defn url-for
  ([url params headers]
   (let [url-for (route/url-for-routes (route/expand-routes components.pedestal/all-routes))]
     (url-for url :params params))))

(defn service-fn
  [pedestal]
  (utils.integration-test/get-component pedestal [:service ::http/service-fn]))

(defn parsee
  [body cn]
  (misc.content-type-parser/transform-content-to body cn))

(defn request!
  ([{:keys [method endpoint params body headers] :or {headers {"Content-Type" "application/json"}}}]
   (flow (str "Requesting -" method " - " endpoint)
     [service (state-flow.api/get-state :pedestal)]
     (let [service (service-fn service)
           {:keys [body] :as json-outcome} (response-for service method (url-for endpoint params headers)
                                 :headers headers
                                 :body (parsee body "application/json"))
           outcome (assoc json-outcome :body (parsee body "application/edn"))]
       (flow/return outcome)))))

(integration-test first-test
  (flow "first test"
    (let [{:keys [status body] :as a} (request! {:method   :get
                                                 :endpoint :api-version
                                                 :params   {:id (str (random-uuid))}})]

      (match? {:status 200} a))))
