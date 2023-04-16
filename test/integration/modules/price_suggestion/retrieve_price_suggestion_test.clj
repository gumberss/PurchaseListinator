(ns modules.price-suggestion.retrieve-price-suggestion-test
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [utils.integration-test :refer [integration-test]]
            [state-flow.core :refer [flow]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [matcher-combinators.matchers :as m]
            [clj-http.client :as c]
            [utils.http])
  (:use clj-http.fake))

(def user-id (random-uuid))
(def item-id (random-uuid))
(def item-2-id (random-uuid))
;(with-fake-routes-in-isolation)
(integration-test retrieve-item-price-suggestion-test

  (flow "Retrieve item price suggestion"
    [response (utils.http/request! {:method               :get
                                    :endpoint             :get-price-suggestion-by-item-id
                                    :token                user-id
                                    :params               {:item-id (str item-id)}
                                    :response-body-schema s/Keyword})]
    (match? {:status 200
             :body   :ok} response)))

(integration-test retrieve-many-items-price-suggestion-test
  (flow "Retrieve item price suggestion"
    [response (utils.http/request! {:method               :get
                                    :endpoint             :get-price-suggestion-by-items-ids
                                    :token                user-id
                                    :params               {:items-ids [item-id item-2-id]}
                                    :response-body-schema s/Keyword})]
    (match? {:status 200
             :body   :ok} response)))