(ns purchase-listinator.adapters.db.purchase-list-management-data
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]))


(s/defn ^:private category->internal
  [db-wire]
  (let [{{:purchase-list/keys [id]} :purchase-list
         items :items :as parsed} (-> (misc.datomic/datomic->entity db-wire)
                                                                   (misc.general/unnamespace-keys))]
    (-> (assoc parsed :purchase-list-id id
                      :items (map identity items))
        (dissoc :purchase-list))))

(s/defn db->categories+items-view
  [db-wire] :- models.internal.purchase-category/PurchaseCategory
  (let [{:keys [id categories]} (-> (misc.datomic/datomic->entity db-wire)
                                    (misc.general/unnamespace-keys))]
    {:id         id
     :categories (map category->internal categories)}))

