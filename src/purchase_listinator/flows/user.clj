(ns purchase-listinator.flows.user
  (:require
    [purchase-listinator.dbs.datomic.user :as dbs.datomic.user]
    [purchase-listinator.misc.general :as misc.general]
    [schema.core :as s]))


(s/defn register
  [user-external-id :- s/Str
   {:keys [datomic]}]
  (let [user-id (dbs.datomic.user/existent? user-external-id datomic)]
    (if (not user-id)
      (dissoc (dbs.datomic.user/upsert {:id          (misc.general/squuid)
                                        :external-id user-external-id} datomic)
              :external-id)
      {:id user-id})))