(ns modules.shopping-events.retrieve-events-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.modules.events.schemas.wires.out.http.shopping-events :as wires.out.http.shopping-events]
            [utils.integration-test :refer [integration-test]]
            [state-flow.core :refer [flow]]
            [utils.event-processing :as events]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [utils.http]))


(def user-id (random-uuid))
(def item-id (random-uuid))

(def event {:id               (random-uuid)
            :moment           123
            :event-type       :some-shopping-event
            :user-id          user-id
            :shopping-id      (random-uuid)
            :item-id          item-id
            :price            10
            :quantity-in-cart 4})

(def events {:events [event]})

(integration-test retrieve-shopping-events-by-item-id-test
  (flow "Receive shopping events"
    (events/publish-event! :purchase-listinator/shopping.finished events))
  (flow "Retrieve events"
    [response (utils.http/request! {:method               :get
                                    :endpoint             :get-events-by-item-id
                                    :token                user-id
                                    :params               {:item-id (str item-id)}
                                    :response-body-schema wires.out.http.shopping-events/EventCollection})]
    (match? {:status 200
             :body   events}
            response)))
