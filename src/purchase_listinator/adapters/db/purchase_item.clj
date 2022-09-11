(ns purchase-listinator.adapters.db.purchase-item
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.purchase-item :as models.internal.purchase-item]))

(s/defn internal->db
  [purchase-list-id :- s/Uuid
   {:keys [category-id] :as internal} :- models.internal.purchase-item/PurchaseItem]
  {:purchase-list/id             purchase-list-id
   :purchase-list/purchase-items (-> internal
                                     (dissoc :category-id)
                                     (assoc :purchase-category {:purchase-category/id category-id})
                                     (misc.general/namespace-keys :purchase-item))})

(s/defn db->internal :- models.internal.purchase-item/PurchaseItem
  [{{category-id :purchase-category/id} :purchase-item/purchase-category :as db-wire}]
  (-> (misc.datomic/datomic->entity db-wire)
      (misc.general/unnamespace-keys)
      (dissoc :purchase-category)
      (assoc :category-id category-id)))
