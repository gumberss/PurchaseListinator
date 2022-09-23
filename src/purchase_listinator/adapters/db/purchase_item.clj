(ns purchase-listinator.adapters.db.purchase-item
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.purchase-item :as models.internal.purchase-item]))

(s/defn internal->db
  [{:keys [category-id] :as internal} :- models.internal.purchase-item/PurchaseItem]
  (-> (dissoc internal :category-id)
      (assoc :category {:purchase-category/id category-id})
      (misc.general/namespace-keys :purchase-item)))

(s/defn db->internal :- models.internal.purchase-item/PurchaseItem
  [db-wire]
  (when (not-empty db-wire)
    (let [{:keys [category] :as parsed} (-> (misc.datomic/datomic->entity db-wire)
                                            (misc.general/unnamespace-keys))]
      (-> (assoc parsed :category-id (:purchase-category/id category))
          (dissoc :category)))))
