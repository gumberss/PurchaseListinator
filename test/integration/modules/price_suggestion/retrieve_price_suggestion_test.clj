(ns modules.price-suggestion.retrieve-price-suggestion-test
  (:require [clojure.test :refer :all]
            [purchase-listinator.misc.date :as misc.date]
            [purchase-listinator.modules.price-suggestion.schemas.wire.out.price-suggestion :as schemas.wire.out.price-suggestion]
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
                                      :path-params          {:item-id (str item-id)}
                                      :response-body-schema s/Keyword})]
      (match? {:status 200
               :body   :ok} response)))




(integration-test retrieve-many-items-price-suggestion-test
 (flow "Setting responses"
   (utils.http/with-response :price-suggestion/http :shopping-events/get-events-by-items :get
                             {:status             200
                              :events-collections [{:item-id item-id
                                                    :events  [{:id          (random-uuid)
                                                               :moment      (misc.date/numb-now)
                                                               :event-type  :change-item
                                                               :shopping-id (random-uuid)
                                                               :price       10}]}
                                                   {:item-id item-2-id
                                                    :events  [{:id          (random-uuid)
                                                               :moment      (misc.date/numb-now)
                                                               :event-type  :change-item
                                                               :shopping-id (random-uuid)
                                                               :price       20}]}]}))
  (flow "Retrieve item price suggestion"
    [response (utils.http/request! {:method               :get
                                    :endpoint             :get-price-suggestion-by-items-ids
                                    :token                user-id
                                    :query-params         {:items-ids [item-id item-2-id]}
                                    :response-body-schema schemas.wire.out.price-suggestion/ShoppingItemSuggestedPrices})]
    (match? {:status 200
             :body   {:price-suggestion
                      [{:item-id uuid?
                        :predicted-date number?,
                        :suggested-price 10.0}
                       {:item-id uuid?
                        :predicted-date number?,
                        :suggested-price 20.0}]}} response)))