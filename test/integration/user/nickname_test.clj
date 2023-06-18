(ns user.nickname-test
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [state-flow.api :as flow]
            [state-flow.core :refer [flow]]
            [utils.integration-test :refer [integration-test]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [utils.http :as utils.http]
            [purchase-listinator.dbs.datomic.purchase-list :as dbs.datomic.purchase-list]))

(def list-id "5215075f-9a24-47e9-91fb-8485adb410f4")
(def user-external-id "Random value")

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
   nickname :- (s/maybe s/Str)]
  (flow (str "Change the nickname of the user " user-id " to " nickname)
    [{:keys [body] :as response} (utils.http/request! {:method               :post
                                                       :endpoint             :nickname-user
                                                       :token                user-id
                                                       :body                 {:nickname nickname}
                                                       :response-body-schema (s/cond-pre s/Str {:id       s/Uuid
                                                                                                :nickname s/Str})})]
    (flow/return response)))

(integration-test change-nickname-test
  (flow "Should change the user nickname"
    [register-user-response (register-user-request! user-external-id)
     {:keys [body] :as change-nickname-response} (change-nickname-request! (-> register-user-response :body :id) "Bro")]
    (match? {:status 200
             :body   {:id       (:id body)
                      :nickname "Bro"}} change-nickname-response)))

(integration-test change-nickname-already-used-test
  (flow "Shouldn't change the user nickname when the nickname is already in use"
    [first-user-response (register-user-request! user-external-id)
     second-user-response (register-user-request! "Second User Id")]

    (flow "Changing the first user nickname"
      [first-nickname-response (change-nickname-request! (-> first-user-response :body :id) "Bro")]
      (match? {:status 200
               :body   {:id       (-> first-nickname-response :body :id)
                        :nickname "Bro"}} first-nickname-response))

    (flow "Try to change the second user nickname for one that is already in use"
      [second-nickname-response (change-nickname-request! (-> second-user-response :body :id) "Bro")]
      (match? {:status 400
               :body   "[[NICKNAME_ALREADY_USED]]"} second-nickname-response))))

(integration-test change-nickname-invalid-test
  (flow "Shouldn't change the user nickname when the nickname is invalid"
    [register-user-response (register-user-request! user-external-id)
     change-nickname-response (change-nickname-request! (-> register-user-response :body :id) "")]
    (match? {:status 400
             :body   "[[INVALID_NICKNAME]]"} change-nickname-response)))