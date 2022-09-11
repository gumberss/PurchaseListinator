(ns purchase-listinator.adapters.db.purchase-list-management-data
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]))

(s/defn internal->db
  [
   {:keys [purchase-list-id] :as internal} :- models.internal.purchase-category/PurchaseCategory]
  (-> internal
      (dissoc :purchase-list-id)
      (assoc :purchase-list {:purchase-list/id purchase-list-id})
      (misc.general/namespace-keys :purchase-category)))

(s/defn db->internal
  [{{id :purchase-list/id} :purchase-category/purchase-list :as db-wire}] :- models.internal.purchase-category/PurchaseCategory
  (-> (misc.datomic/datomic->entity db-wire)
      (misc.general/unnamespace-keys)
      (assoc :purchase-list id)))

