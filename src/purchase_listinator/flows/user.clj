(ns purchase-listinator.flows.user
  (:require
    [purchase-listinator.dbs.datomic.user :as dbs.datomic.user]
    [purchase-listinator.logic.errors :as logic.errors]
    [purchase-listinator.misc.general :as misc.general]
    [purchase-listinator.logic.user :as logic.user]
    [cats.monad.either :refer [left]]
    [schema.core :as s]))

(s/defn register
  [user-external-id :- s/Str
   {:keys [datomic]}]
  (if-let [user (dbs.datomic.user/find-by-external-id user-external-id datomic)]
    (dissoc user :external-id)
    (dissoc (dbs.datomic.user/upsert {:id          (misc.general/squuid)
                                      :external-id user-external-id} datomic)
            :external-id)))

(s/defn set-nickname
  [nickname :- (s/maybe s/Str)
   user-id :- s/Uuid
   {:keys [datomic]}]
  (if (not (logic.user/valid-nickname? nickname))
    (left (logic.errors/build 400 "[[INVALID_NICKNAME]]"))
    (try
      (dbs.datomic.user/upsert
        {:id       user-id
         :nickname nickname} datomic)
      (catch Exception ex
        (left
          (if (= :transact/unique (-> ex ex-data :error))
            (logic.errors/build 400 "[[NICKNAME_ALREADY_USED]]")
            (do (clojure.pprint/pprint ex)
                (logic.errors/build 500 "An error occurred on the server"))))))))
