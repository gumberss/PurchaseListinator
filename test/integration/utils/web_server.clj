(ns utils.web-server
  (:require [io.pedestal.http :as http]
            [purchase-listinator.components.pedestal :as components.pedestal]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :refer [response-for]]
            [com.stuartsierra.component :as component]
            [purchase-listinator.core :as core]
            [clojure.test :refer :all]
            [io.pedestal.test :as pt]
            [state-flow.api :as state-flow.api]
            [state-flow.cljtest :as state-flow.cljtest]
            [state-flow.core :as state-flow :refer [flow run*]]
            [state-flow.api :refer [defflow]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [schema.core :as s]
            [state-flow.api :as flow]))

(defn url-for
  ([url]
   (url-for url nil))
  ([url params]
   (url-for url params {"Content-Type" "application/json"}))
  ([url params headers]
   (let [url-for (route/url-for-routes (route/expand-routes components.pedestal/all-routes))]
     (url-for url :headers headers
              :params params))))

(defn get-component
  [system & component-path]
  (get-in system (flatten component-path)))

(defn service-fn
  [pedestal]
  (get-component pedestal [:service ::http/service-fn]))

(def system-config
  {:env        :test
   :web-server {:port 5150
                :host "192.168.1.104"}
   :datomic    {:db-uri (str "datomic:mem://" (random-uuid))}})


(defn run-now
  [flow state]
  (s/with-fn-validation (state-flow/run flow state)))

(defn start-system
  []
  (-> (core/new-system-test system-config)
      component/start))

(defmacro my-defflow
  [name & flows]
  `(state-flow.cljtest/defflow ~name {:init   start-system
                                      :runner run-now}
                               ~@flows))

(defn request!
  [{:keys [method endpoint params]}]
  (flow (str "Requesting -" method " - " endpoint)
        [service (state-flow.api/get-state :pedestal)]
        (let [service (service-fn service)
              outcome (response-for service method (url-for endpoint params))]

          (flow/return outcome))))

(my-defflow first-test
            (flow "first test"
                  (let [{:keys [status body] :as a} (request! {:method   :get
                                                               :endpoint :api-version
                                                               :params   {:id (str (random-uuid))}})]

                    (match? {:status 200} a))))
