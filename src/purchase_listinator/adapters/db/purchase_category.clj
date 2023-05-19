(ns purchase-listinator.adapters.db.purchase-category
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.purchase-list.purchase-category :as models.internal.purchase-category]))

(s/defn internal->db
  [{:keys [purchase-list-id order-position color] :as internal} :- models.internal.purchase-category/PurchaseCategory]
  (-> (dissoc internal :purchase-list-id)
      (assoc :purchase-list {:purchase-list/id purchase-list-id}
             :order-position (long order-position)
             :color (long color))
      (misc.general/namespace-keys :purchase-category)))

(s/defn db->internal :- (s/maybe models.internal.purchase-category/PurchaseCategory)
  [db-wire]
  (when (not-empty db-wire)
    (let [{:keys [purchase-list] :as internal} (-> (misc.datomic/datomic->entity db-wire)
                                                   (misc.general/unnamespace-keys))]
      (-> (assoc internal :purchase-list-id (:purchase-list/id purchase-list))
          (dissoc :purchase-list)))))

