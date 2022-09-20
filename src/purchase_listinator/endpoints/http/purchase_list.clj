(ns purchase-listinator.endpoints.http.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.wires.out.purchase-list :as out.purchases-lists]
            [purchase-listinator.flows.purchase-list :as flows.purchase-list]
            [purchase-listinator.adapters.in.purchase-list :as adapters.in.purchase-list]
            [purchase-listinator.adapters.misc :as adapters.misc]
            [purchase-listinator.adapters.in.purchase-category :as adapters.in.purchase-category]
            [purchase-listinator.adapters.in.purchase-item :as adapters.in.purchase-item]
            [purchase-listinator.flows.purchase-category :as flows.purchase-category]
            [purchase-listinator.flows.purchase-item :as flows.purchase-item]
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
  (branch (misc.either/try-right
            (-> (adapters.in.purchase-list/wire->internal wire)
                (flows.purchase-list/create datomic)))
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
  [{{datomic :datomic}       :component
    wire                     :json-params
    {:keys [id category-id]} :path-params}]
  (branch (misc.either/try-right
            (let [purchase-list-id (adapters.misc/string->uuid id)
                  purchase-category-id (adapters.misc/string->uuid category-id)
                  internal-item (adapters.in.purchase-item/wire->internal wire)]
              (flows.purchase-item/create purchase-list-id purchase-category-id internal-item datomic)))
          ->Error
          ->Success))

(s/defn add-purchases-lists-category
  [{{datomic :datomic} :component
    wire               :json-params}]
  (branch (misc.either/try-right
            (-> (adapters.in.purchase-category/wire->internal wire)
                (flows.purchase-category/create datomic)))
          ->Error
          ->Success))

(s/defn change-category-order
  [{{datomic :datomic}                  :component
    {:keys [old-position new-position id]} :path-params}]
  (branch (misc.either/try-right
            (let [list-id (adapters.misc/string->uuid id)
                  old-position (adapters.misc/string->integer old-position)
                  new-position (adapters.misc/string->integer new-position)]
              (flows.purchase-category/change-categories-order list-id old-position new-position datomic)))
          ->Error
          ->Success))

(s/defn purchases-lists-management-data
  [{{datomic :datomic} :component
    {id :id}           :path-params}]
  (branch (misc.either/try-right
            (-> (adapters.misc/string->uuid id)
                (flows.purchase-list/management-data datomic)))
          ->Error
          ->Success))

;todo: /lists should return only the purchase list data and /lists/:id should return items and categories too
(def routes
  #{["/api/purchases/lists" :get [get-purchase-lists] :route-name :get-purchases-lists]
    ["/api/purchases/lists" :post [post-purchase-lists] :route-name :post-purchases-lists]
    ["/api/purchases/lists" :put [edit-purchase-lists] :route-name :edit-purchases-lists]
    ["/api/purchases/lists/:id" :delete [disable-purchase-lists] :route-name :disable-purchases-lists]
    ["/api/purchases/lists/:id/category/:category-id/add/item" :post [add-purchases-lists-item] :route-name :add-purchases-lists-item]
    ["/api/purchases/lists/:id/add/category" :post [add-purchases-lists-category] :route-name :add-purchases-lists-category]
    ["/api/purchases/lists/:id/categories/changeOrder/:old-position/:new-position" :post [change-category-order] :route-name :change-category-order]
    ["/api/purchases/lists/:id/managementData" :get [purchases-lists-management-data] :route-name :purchases-lists-management-data]})
