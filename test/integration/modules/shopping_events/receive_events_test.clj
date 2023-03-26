(ns modules.shopping-events.receive-events-test
  (:require [clojure.test :refer :all]
            [utils.integration-test :refer [integration-test]]
            [state-flow.core :refer [flow]]
            [utils.event-processing :as events]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [purchase-listinator.modules.events.diplomat.db.shopping-events :as diplomat.db.shopping-events]))

(def user-id (random-uuid))

(def event {:id          (random-uuid)
            :moment      123
            :event-type  :some-shopping-event
            :user-id     user-id
            :shopping-id (random-uuid)})

(def events {:events [event]})

(integration-test receive-shopping-events-test
  (flow "Receive shopping events"
    (events/publish-event! :purchase-listinator/shopping.finished
                           events))
  (flow "Validating insertion"
    [main-db (state-flow.api/get-state :shopping-events/main-db)]
    (match? [event] (diplomat.db.shopping-events/get-by-user-id user-id main-db))))
