(ns purchase-listinator.adapters.db.purchase-category
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.adapters.db.purchase-item :as adapters.db.purchase-item]))

(s/defn add-item->db [category-id
                      internal-item]
  {:purchase-category/id                  category-id
   :purchase-category/purchase-items [(adapters.db.purchase-item/internal->db internal-item)]})


(s/defn internal->db
  [internal :- models.internal.purchase-category/PurchaseCategory]
  (misc.general/namespace-keys internal :purchase-category))

(s/defn db->internal :- models.internal.purchase-category/PurchaseCategory
  [db-wire]
  (when db-wire
    (let [{:keys [purchase-items] :as internal} (-> (misc.datomic/datomic->entity db-wire)
                                                    (misc.general/unnamespace-keys))]
      (assoc internal :purchase-items (map adapters.db.purchase-item/db->internal purchase-items)))))

