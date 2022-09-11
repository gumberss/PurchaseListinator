(ns purchase-listinator.adapters.db.purchase-list-management-data
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]))

(s/defn db->internal
  [purchase-list-id
   db-wire] :- models.internal.purchase-category/PurchaseCategory
  (let [{:keys [purchase-categories] :as k} (-> (misc.datomic/datomic->entity db-wire)
                                          (misc.general/unnamespace-keys))]
    {:id         purchase-list-id
     :categories (map misc.general/unnamespace-keys purchase-categories)}))

