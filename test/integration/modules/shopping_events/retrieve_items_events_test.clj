(ns modules.shopping-events.retrieve-items-events-test
  (:require
    [clojure.test :refer :all]
    [purchase-listinator.modules.events.schemas.wires.out.http.shopping-item-events :as wires.out.http.shopping-item-events]
    [utils.integration-test :refer [integration-test]]
    [state-flow.core :refer [flow]]
    [utils.event-processing :as events]
    [state-flow.assertions.matcher-combinators :refer [match?]]
    [matcher-combinators.matchers :as m]
    [utils.http]))

(def user-id (random-uuid))
(def item-id (random-uuid))
(def item-2-id (random-uuid))
(def shopping-id (random-uuid))

(def event {:id               (random-uuid)
            :moment           123
            :event-type       :some-shopping-event
            :user-id          user-id
            :shopping-id      shopping-id
            :item-id          item-id
            :price            10
            :quantity-in-cart 4})

(def event-2
  (assoc event :id (random-uuid)
               :item-id item-2-id))

(def other-item-event
  (assoc event :id (random-uuid)
               :item-id (random-uuid)))

(def item-events {:item-id item-id
                  :events  [event]})
(def item-2-events {:item-id item-2-id
                    :events  [event-2]})

(def events {:events [event]})

(def shopping-cart-closed-event
  {:purchase-list-id (random-uuid)
   :shopping-id shopping-id
   :cart-events      [event]})

(integration-test retrieve-shopping-item-events-test
  (flow "Receive shopping events"
    (events/publish-event! :shopping-cart/shopping-cart.closed shopping-cart-closed-event))
  (flow "Retrieve events"
    [response (utils.http/request! {:method               :get
                                    :endpoint             :get-events-by-item-id
                                    :token                user-id
                                    :path-params         {:item-id (str item-id)}
                                    :response-body-schema wires.out.http.shopping-item-events/ShoppingItemEventCollection})]
    (match? {:status 200
             :body   events} response)))

(def many-events [event event-2 other-item-event])

(def shopping-cart-closed-many-events-event
  {:purchase-list-id (random-uuid)
   :shopping-id shopping-id
   :cart-events      many-events})
(def events-collections-result
  {:events-collections (m/in-any-order [item-events item-2-events])})

(integration-test retrieve-shopping-items-events-test
  (flow "Receive shopping events"
    (events/publish-event! :shopping-cart/shopping-cart.closed shopping-cart-closed-many-events-event))
  (flow "Retrieve events"
    [response (utils.http/request! {:method               :get
                                    :endpoint             :get-events-by-items
                                    :token                user-id
                                    :query-params         {:items-ids [(str item-id) (str item-2-id)]}
                                    :response-body-schema wires.out.http.shopping-item-events/ShoppingItemEventsResult})]
    (match? {:status 200
             :body   events-collections-result} response)))
