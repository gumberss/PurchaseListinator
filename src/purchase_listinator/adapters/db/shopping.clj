(ns purchase-listinator.adapters.db.shopping
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]))

(s/defn internal->db
  [{:keys [list-id] :as internal} :- models.internal.shopping/Shopping]
  (-> internal
      (assoc :list {:purchase-list/id list-id})
      (dissoc :list-id :categories)
      (misc.general/namespace-keys :shopping)))

(s/defn db->internal :- models.internal.shopping/Shopping
  [{:shopping/keys [list] :as db-wire}]
  (when (not-empty db-wire)
    (-> (misc.datomic/datomic->entity db-wire)
        (misc.general/unnamespace-keys)
        (assoc :list-id (:purchase-list/id list))
        (dissoc :list))))
