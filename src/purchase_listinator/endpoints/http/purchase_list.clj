(ns purchase-listinator.endpoints.http.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.wires.out.purchase-list :as out.purchases-lists]
            [purchase-listinator.flows.purchase-list :as flows.purchase-list]
            [purchase-listinator.adapters.in.purchase-list :as adapter.in]
            [taoensso.carmine :as car]
            [cats.monad.either :refer :all]))

(defn namesss
  [{{{:keys [connection]} :redis} :component}]
  {:status 200
   :body   {:oi (try (car/wcar connection (car/get "lala"))
                     (catch Exception e
                       (println e)))}})

(s/defn ->200
  [data]
  {:status 200
   :body   data})

(s/defn ->500
  [{:keys [status body] :as err}]
  (println err)
  {:status (or status 500)
   :body   (or body err)})

(s/defn get-purchase-lists :- {:status s/Int
                               :body   out.purchases-lists/PurchaseList}
  [{{:keys [datomic]} :component}]
  (branch (flows.purchase-list/get-lists datomic)
          ->500
          ->200))

(s/defn post-purchase-lists :- {:status s/Int
                                :body   {}}
  [{{:keys [datomic]} :component
    :keys             [json-params]}]
  (branch (-> (adapter.in/wire->internal json-params)
              (flows.purchase-list/create-list datomic))
          ->500
          ->200))

(def routes
  #{["/purchases/lists" :get [get-purchase-lists] :route-name :get-purchases-lists]
    ["/purchases/lists" :post [post-purchase-lists] :route-name :post-purchases-lists]
    ["/name" :post [namesss] :route-name :name]
    ["/name" :get [namesss] :route-name :get-name]
    ["/name" :put [namesss] :route-name :put-name]})