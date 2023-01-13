(ns purchase-listinator.adapters.db.shopping-event
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]))

(s/defn internal->db
  [{:keys [event-id] :as internal} :- models.internal.shopping-cart/CartEvent]
  (-> internal
      (assoc :id event-id)
      (dissoc :event-id)
      (misc.general/namespace-keys :shopping-event)))

(s/defn db->internal :- models.internal.shopping-cart/CartEvent
  [{:cart-event/keys [id] :as db-wire}]
  (when (not-empty db-wire)
    (-> (misc.datomic/datomic->entity db-wire)
        (misc.general/unnamespace-keys)
        (assoc :event-id id))))
