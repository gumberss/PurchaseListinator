(ns purchase-listinator.components.pedestal
  (:require
    [clojure.string :as str]
    [com.stuartsierra.component :as component]
    [io.pedestal.http :as http]
    [purchase-listinator.endpoints.http.purchase-list :as http.purchase-list]
    [purchase-listinator.endpoints.http.shopping :as http.shopping]
    [io.pedestal.http.body-params :as body-params]
    [purchase-listinator.misc.pedestal :as misc.pedestal]
    [camel-snake-kebab.core :as csk]))
(defn version [_]
  {:status 200
   :body   {:version :v1}})


(def default-routes
  #{["/api/version" :get [version] :route-name :api-version]})

(def all-routes
  (set (concat default-routes
               http.purchase-list/routes
               http.shopping/routes)))

(defn test?
  [service-map]
  (= :test (:env service-map)))

(defn assoc-component [component context]
  (update context :request assoc :component component))

(defn inject-component [component]
  {:name  :component-injector
   :enter (partial assoc-component component)})

(defn assoc-identifier [{{:keys [headers]} :request :as context}]
  (update context :request assoc :user-id (some-> (get headers "authorization")
                                                  (str/replace "Bearer " ""))))
(def inject-user-identifier
  {:name  :inject-user-identifier
   :enter assoc-identifier})

(defn validate-user [{{:keys [user-id]} :request :as context}]
  (if user-id
    context
    (assoc context :response {:status 403
                              :headers {}})))
(def validate-user-identifier
  {:name  :validate-user-identifier
   :enter validate-user})

(defn interceptors [components]
  [misc.pedestal/service-error-handler
   inject-user-identifier
   validate-user-identifier
   (body-params/body-params
     (body-params/default-parser-map :json-options {:key-fn csk/->kebab-case-keyword}))
   misc.pedestal/coerce-body-content-type
   (inject-component components)])

(defn include-interceptors
  [endpoint component]
  (update-in endpoint [2] #(into (interceptors component) %)))

(defn add-interceptors
  [endpoints component]
  (set (map #(include-interceptors % component) endpoints)))

(defrecord Pedestal [service-map
                     service]
  component/Lifecycle
  (start [this]
    (if service
      this
      (cond-> service-map
              true (assoc ::http/routes (add-interceptors all-routes this))
              true http/create-server
              (not (test? service-map)) http/start
              true ((partial assoc this :service)))))
  (stop [this]
    (when (and service #_(not (test? service-map)))
      (http/stop service))
    (assoc this :service nil)))

(defn new-pedestal
  []
  (map->Pedestal {}))
