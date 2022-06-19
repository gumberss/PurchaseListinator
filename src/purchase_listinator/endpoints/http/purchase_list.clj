(ns purchase-listinator.endpoints.http.purchase-list)


(defn namesss
  [request]
  {:status 200
   :body   {:oi :lala}})

(def routes
  #{["/greet" :get [namesss] :route-name :greet]
    ["/test" :get [namesss] :route-name :test]
    ["/name" :post [namesss] :route-name :name]
    ["/name" :get [namesss] :route-name :get-name]
    ["/name" :put [namesss] :route-name :put-name]})