(ns purchase-list.share-test
  (:require [clojure.test :refer :all]
            [state-flow.api :as flow]
            [state-flow.core :refer [flow]]
            [schema.core :as s]
            [utils.integration-test :refer [integration-test]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [purchase-listinator.dbs.datomic.share :as dbs.datomic.share]
            [utils.http :as utils.http]))
(def user-external-id "Random value")
(def user-nickname "Bro")

(s/defn register-user-request!
  [user-external-id :- s/Str]
  (flow "Registering a user"
    [{:keys [body] :as response} (utils.http/request! {:method               :post
                                                       :endpoint             :register-user
                                                       :token                user-external-id
                                                       :body                 {:user-external-id user-external-id}
                                                       :response-body-schema {:id   s/Uuid
                                                                              s/Any s/Any}})]
    (match? {:status 200
             :body   {:id uuid?}} response)

    (flow/return response)))

(s/defn change-nickname-request!
  [user-id :- s/Uuid
   nickname :- (s/maybe s/Str)
   response-body-schema]
  (flow (str "Change the nickname of the user " user-id " to " nickname)
    [{:keys [body] :as response} (utils.http/request! {:method               :post
                                                       :endpoint             :nickname-user
                                                       :token                user-id
                                                       :body                 {:nickname nickname}
                                                       :response-body-schema response-body-schema})]
    (flow/return response)))

(integration-test share-list-test
  (flow "with registered users"
    [{{user-id-1 :id} :body} (register-user-request! user-external-id)
     {{user-id-2 :id} :body} (register-user-request! "Other user")
     _ (change-nickname-request! user-id-2 user-nickname {:id s/Uuid :nickname s/Str})]
    (flow "with created list"
      [{{list-id :id} :body :as response} (utils.http/request! {:method   :post
                                                                :endpoint :post-purchases-lists
                                                                :token    user-id-1
                                                                :body     {:name "List 3"}})]
      (match? {:status 200 :body {:id string?}} response)

      (flow "Should share the list with other customer"
        [{:keys [body] :as response} (utils.http/request! {:method   :post
                                                           :endpoint :share-list
                                                           :token    user-id-1
                                                           :body     {:list-id           list-id
                                                                      :customer-nickname user-nickname}})]
        (match? {:status 200} response))
      (flow "Should have the list share saved on the database"
        [datomic (state-flow.api/get-state :datomic)]
        (match? [{:customer-id user-id-2,
                  :list-id     (parse-uuid list-id)}]
                 (dbs.datomic.share/get-by-list-id (parse-uuid list-id) datomic))))))
