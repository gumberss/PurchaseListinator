(ns utils.event-processing
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [state-flow.api :as state-flow.api]
            [state-flow.core :refer [flow]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [state-flow.api :as flow]
            [utils.integration-test :refer [integration-test]]))

(defn ^:private process-event!
  [{:keys [subscribers] :as rabbitmq}
   exchange
   event]
  (->> subscribers
       (filter #(= exchange (:exchange %)))
       first
       :handler
       (#(% {} {} rabbitmq event))))


(s/defn publish-event! :- s/Any
  [exchange :- s/Keyword
   event :- s/Any]
  (flow (str "Processing event in exchange " exchange)
    [rabbitmq (state-flow.api/get-state :shopping-events/rabbitmq)]
    (let [outcome (process-event! rabbitmq exchange event)]
      (flow/return outcome))))