(ns purchase-listinator.endpoints.http.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.wires.out.purchase-list :as out.purchases-lists]
            [purchase-listinator.flows.purchase-list :as flows.purchase-list]
            [purchase-listinator.adapters.in.purchase-list :as adapters.in.purchase-list]
            [purchase-listinator.adapters.misc :as adapters.misc]
            [purchase-listinator.adapters.in.purchase-category :as adapters.in.purchase-category]
            [purchase-listinator.flows.purchase-category :as flows.purchase-category]
            [purchase-listinator.misc.either :as misc.either]
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
  (branch (-> (adapters.in.purchase-list/wire->internal wire)
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
    wire               :json-params}]
  (branch (-> (adapters.in.purchase-list/wire->internal wire)
              (flows.purchase-list/edit datomic))
          ->Error
          ->Success))

(s/defn add-purchases-lists-item
  [{{datomic :datomic} :component
    wire               :json-params}]
  {})

(s/defn add-purchases-lists-category
  [{{datomic :datomic} :component
    wire               :json-params}]
  (branch (misc.either/try-right
            (-> (adapters.in.purchase-category/wire->internal wire)
                (flows.purchase-category/create datomic)))
          ->Error
          ->Success))

(s/defn purchases-lists-management-data
  [{{datomic :datomic} :component
    wire               :json-params}]
  {})

;todo: /lists should return only the purchase list data and /lists/:id should return items and categories too
(def routes
  #{["/api/purchases/lists" :get [get-purchase-lists] :route-name :get-purchases-lists]
    ["/api/purchases/lists" :post [post-purchase-lists] :route-name :post-purchases-lists]
    ["/api/purchases/lists" :put [edit-purchase-lists] :route-name :edit-purchases-lists]
    ["/api/purchases/lists/:id" :delete [disable-purchase-lists] :route-name :disable-purchases-lists]
    ["/api/purchases/lists/:id/add/item" :post [add-purchases-lists-item] :route-name :add-purchases-lists-item]
    ["/api/purchases/lists/:id/add/category" :post [add-purchases-lists-category] :route-name :add-purchases-lists-category]
    ["/api/purchases/lists/:id/managementData" :post [purchases-lists-management-data] :route-name :purchases-lists-management-data]})