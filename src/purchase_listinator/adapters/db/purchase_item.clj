(ns purchase-listinator.adapters.db.purchase-item
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.purchase-item :as models.internal.purchase-item]))

(s/defn internal->db
  [purchase-category-id :- s/Uuid
   internal :- models.internal.purchase-item/PurchaseItem]
  {:purchase-category/id             purchase-category-id
   :purchase-category/purchase-items (misc.general/namespace-keys internal :purchase-item)})

(s/defn db->internal :- models.internal.purchase-item/PurchaseItem
  [db-wire]
  (when db-wire
    (-> (misc.datomic/datomic->entity db-wire)
        (misc.general/unnamespace-keys))))
