(ns purchase-listinator.adapters.db.shopping-event
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]))

(s/defn internal->db
  [internal :- models.internal.shopping-cart/CartEvent]
  (misc.general/namespace-keys internal :shopping-event))

(s/defn db->internal :- models.internal.shopping-cart/CartEvent
  [{:cart-event/keys [id] :as db-wire}]
  (when (not-empty db-wire)
    (-> (misc.datomic/datomic->entity db-wire)
        (misc.general/unnamespace-keys)
        (assoc :event-id id))))
