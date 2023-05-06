(ns utils.http
  (:require
    [clojure.data.json :as json]
    [clj-http.client :as client]
    [clojure.string :as str]
    [io.pedestal.http :as http]
    [purchase-listinator.components.pedestal :as components.pedestal]
    [io.pedestal.http.route :as route]
    [io.pedestal.test :refer [response-for]]
    [clojure.test :refer :all]
    [schema.coerce :as coerce]
    [state-flow.api :as state-flow.api]
    [state-flow.core :refer [flow]]
    [state-flow.assertions.matcher-combinators :refer [match?]]
    [state-flow.api :as flow]
    [utils.integration-test :refer [integration-test]]
    [purchase-listinator.misc.content-type-parser :as misc.content-type-parser]))

(defn url-for
  ([url params]
   (let [url-for (route/url-for-routes (route/expand-routes purchase-listinator.core/routes))]
     (url-for url :path-params params))))

(defn service-fn
  [pedestal]
  (utils.integration-test/get-component pedestal [:service ::http/service-fn]))

(defn parse
  [body cn]
  (misc.content-type-parser/transform-content-to body cn))

(def default-token (random-uuid))

(defn request!
  ([{:keys [method endpoint path-params query-params body headers token response-body-schema] :or {headers {"Content-Type" "application/json"}}}]
   (flow (str "Requesting -" method " - " endpoint)
     [service (state-flow.api/get-state :pedestal)]
     (let [service (service-fn service)
           token (or token default-token)
           query-params-str (when query-params (str "?" (client/generate-query-string query-params)))
           url (str (url-for endpoint path-params) query-params-str)
           _ (println url)
           headers (assoc headers "authorization" (str "Bearer " token)
                                  "user-id" (str token))
           coerce-function (if response-body-schema (coerce/coercer response-body-schema coerce/json-coercion-matcher) identity)
           {:keys [body] :as json-outcome} (response-for service method url
                                                         :headers headers
                                                         :body (parse (or body {}) "application/json"))
           outcome (assoc json-outcome :body (-> (parse body "application/edn")
                                                 (coerce-function)))]
       (flow/return outcome)))))

(integration-test check-version-test
  (flow "first test"
    (let [response (request! {:method   :get
                              :endpoint :api-version
                              :path-params   {:id (str (random-uuid))}})]

      (match? {:status 200} response))))
