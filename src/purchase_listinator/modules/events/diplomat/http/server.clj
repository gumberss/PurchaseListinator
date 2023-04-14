(ns purchase-listinator.modules.events.diplomat.http.server
  (:require
    [clojure.data.json :as json]
    [purchase-listinator.adapters.misc :as adapters.misc]
    [purchase-listinator.misc.either :as misc.either]
    [purchase-listinator.misc.http :as misc.http]
    [purchase-listinator.modules.events.flows.retrieve-events :as flows.retrieve-events]
    [purchase-listinator.modules.events.adapters.out.shopping-item-events :as out.shopping-item-events]
    [purchase-listinator.modules.events.schemas.wires.out.http.shopping-item-events :as wires.out.http.shopping-item-events]
    [schema.core :as s]))

(s/defn get-events-by-item-id :- {:status s/Int
                                  :body   wires.out.http.shopping-item-events/ShoppingItemEventCollection}
  [{:keys              [component]
    {item-id :item-id} :path-params}]
  (misc.http/default-branch
    (misc.either/try-right
      (-> (adapters.misc/string->uuid item-id)
          (flows.retrieve-events/get-items-by-item-id component)
          (out.shopping-item-events/internal->wire)))))

(s/defn get-events-by-items :- {:status s/Int
                                :body   wires.out.http.shopping-item-events/ShoppingItemEventsResult}
  [{:keys               [component]
    {:keys [items-ids]} :params}]
  (misc.http/default-branch
    (misc.either/try-right
      (-> (json/read-str items-ids)
          (->> (map adapters.misc/string->uuid))
          (flows.retrieve-events/get-items-by-items component)
          (out.shopping-item-events/internal-collections->wire-result)))))

(def routes
  #{["/api/events/by/item/:item-id" :get [get-events-by-item-id] :route-name :get-events-by-item-id]
    ["/api/events/by/items" :get [get-events-by-items] :route-name :get-events-by-items]})

