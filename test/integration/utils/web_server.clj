(ns utils.web-server
  (:require [io.pedestal.http :as http]
            [purchase-listinator.components.pedestal :as components.pedestal]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :refer [response-for]]
            [com.stuartsierra.component :as component]
            [purchase-listinator.core :as core]
            [clojure.test :refer :all]
            [cheshire.core :as json]
            [clojure.string :as string]
            [io.pedestal.test :as pt]
            [state-flow.api :as state-flow.api]
            [state-flow.core :as state-flow :refer [flow]]
            [state-flow.api :refer [defflow]]
            [state-flow.assertions.matcher-combinators :refer [match?]]))

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
  [system]
  (get-in system [:pedestal :service ::http/service-fn]))

(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))

(def system-config
  {:env        :test
   :web-server {:port 5150
                :host "192.168.1.104"}
   :datomic    {:db-uri (str "datomic:mem://" (random-uuid))}})

(defflow greeting-test
  (with-system [sut (core/new-system-test system-config)]
    (flow "lala"
          (let [service (service-fn sut)
                {:keys [status body]} (response-for service :delete (url-for :disable-purchases-lists
                                                                             {:id (str (random-uuid))}
                                                                             {"Content-Type" "application/json"}))]
            (match? 200 status)))))

