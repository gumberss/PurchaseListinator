(ns purchase-listinator.endpoints.http.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.wires.out.purchase-list :as out.purchases-lists]
            [purchase-listinator.flows.purchase-list :as flows.purchase-list]
            [purchase-listinator.adapters.in.purchase-list :as adapter.in.purchase-list]
            [purchase-listinator.adapters.misc :as adapters.misc]
            [cats.monad.either :refer :all]))

(s/defn ->Error
  [{:keys [status error] :as err}]
  (println err)
  {:status (or status 500)
   :body   (or error err)})

(s/defn ->Success
  [data]
  (if (left? data)
    (->Error data)
    {:status 200
     :body   data}))

(s/defn get-purchase-lists :- {:status s/Int
                               :body   out.purchases-lists/PurchaseList}
  [{{:keys [datomic]} :component}]
  (branch (flows.purchase-list/get-lists datomic)
          ->Error
          ->Success))

(s/defn post-purchase-lists :- {:status s/Int
                                :body   {}}
  [{{datomic :datomic} :component
    wire               :json-params}]
  (branch (-> (adapter.in.purchase-list/wire->internal wire)
              (flows.purchase-list/create datomic))
          ->Error
          ->Success))

(s/defn disable-purchase-lists :- {:status s/Int
                                   :body   {}}
  [{{:keys [datomic]} :component
    {id :id}          :path-params}]
  (branch (-> (adapters.misc/string->uuid id)
              (flows.purchase-list/disable datomic))
          ->Error
          ->Success))

(s/defn edit-purchase-lists :- {:status s/Int
                                :body   {}}
  [{{datomic :datomic} :component
    wire        :json-params}]
  (branch (-> (adapter.in.purchase-list/wire->internal wire)
              (flows.purchase-list/edit datomic))
          ->Error
          ->Success))

(def routes
  #{["/api/purchases/lists" :get [get-purchase-lists] :route-name :get-purchases-lists]
    ["/api/purchases/lists" :post [post-purchase-lists] :route-name :post-purchases-lists]
    ["/api/purchases/lists" :put [edit-purchase-lists] :route-name :edit-purchases-lists]
    ["/api/purchases/lists/:id" :delete [disable-purchase-lists] :route-name :disable-purchases-lists]})