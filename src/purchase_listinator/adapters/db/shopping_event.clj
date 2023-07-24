(ns purchase-listinator.adapters.db.shopping-event
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]))

(s/defn internal->db
  [{:keys [quantity quantity-changed new-position order-position price] :as internal} :- models.internal.shopping-cart/CartEvent]
  (-> (misc.general/assoc-some internal
                               :quantity (some-> quantity long)
                               :quantity-changed (some-> quantity-changed long)
                               :new-position (some-> new-position long)
                               :order-position (some-> order-position long)
                               :price (some-> price float))
      (misc.general/namespace-keys :shopping-event)))

(s/defn db->internal :- models.internal.shopping-cart/CartEvent
  [db-wire]
  (when (not-empty db-wire)
    (-> (misc.datomic/datomic->entity db-wire)
        (misc.general/unnamespace-keys))))
