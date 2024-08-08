(ns purchase-listinator.modules.shopping.adapters.db.datomic.shopping
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.modules.shopping.schemas.models.shopping :as models.shopping]))

(s/defn internal->db
  [internal :- models.shopping/Shopping]
  (-> (dissoc internal :categories)
      (misc.general/namespace-keys :shopping)))

(s/defn db->internal :- models.shopping/Shopping
  [db-wire]
   (when (not-empty db-wire)
    (-> (misc.datomic/datomic->entity db-wire)
        (misc.general/unnamespace-keys))))
