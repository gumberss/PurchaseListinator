(ns purchase-listinator.modules.events.diplomat.http.server
  (:require
    [purchase-listinator.adapters.misc :as adapters.misc]
    [purchase-listinator.misc.either :as misc.either]
    [purchase-listinator.misc.http :as misc.http]
    [purchase-listinator.modules.events.flows.retrieve-events :as flows.retrieve-events]
    [purchase-listinator.modules.events.adapters.out.shopping-events :as adapters.out.shopping-events]
    [purchase-listinator.modules.events.schemas.wires.out.http.shopping-events :as wires.out.http.shopping-events]
    [schema.core :as s]))

(s/defn get-events-by-item-id :- {:status s/Int
                                  :body   wires.out.http.shopping-events/EventCollection}
  [{:keys              [component]
    {item-id :item-id} :path-params}]
  (misc.http/default-branch
    (misc.either/try-right
      (-> (adapters.misc/string->uuid item-id)
          (flows.retrieve-events/get-items-by-item-id component)
          (adapters.out.shopping-events/internal->wire)))))


(def routes
  #{["/api/events/by/item/:item-id" :get [get-events-by-item-id] :route-name :get-events-by-item-id]})

