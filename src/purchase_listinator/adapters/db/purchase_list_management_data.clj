(ns purchase-listinator.adapters.db.purchase-list-management-data
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.purchase-list.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.adapters.db.purchase-category :as adapters.db.purchase-category]
            [purchase-listinator.adapters.db.purchase-item :as adapters.db.purchase-item]))

(s/defn ^:private category->internal
  [{:purchase-category/keys [items] :as db-category}]
  (-> (adapters.db.purchase-category/db->internal db-category)
      (assoc :items (map adapters.db.purchase-item/db->internal items))))

(s/defn db->categories+items-view
  [db-wire] :- models.internal.purchase-category/PurchaseCategory
  (let [{:keys [id categories]} (-> (misc.datomic/datomic->entity db-wire)
                                    (misc.general/unnamespace-keys))]
    (when db-wire
      {:id         id
       :categories (map category->internal categories)})))

