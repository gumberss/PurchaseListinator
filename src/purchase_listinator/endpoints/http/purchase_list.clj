(ns purchase-listinator.endpoints.http.purchase-list
  (:require [taoensso.carmine :as car]))


(defn namesss
  [{{ {:keys [connection]} :redis} :component}]
  {:status 200
   :body   {:oi (try (car/wcar connection (car/get "lala"))
                     (catch Exception e
                       (println e)))}})

(def routes
  #{["/greet" :get [namesss] :route-name :greet]
    ["/test" :get [namesss] :route-name :test]
    ["/name" :post [namesss] :route-name :name]
    ["/name" :get [namesss] :route-name :get-name]
    ["/name" :put [namesss] :route-name :put-name]})