(ns purchase-listinator.adapters.db.shopping-item
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.shopping-item :as models.internal.shopping-item]))

(s/defn internal->db
  [{:keys [category-id order-position] :as internal} :- models.internal.shopping-item/ShoppingItem]
  (-> (dissoc internal :category-id)
      (assoc :category {:shopping-category/id category-id}
             :order-position (long order-position))
      (misc.general/namespace-keys :shopping-item)))

(s/defn db->internal :- models.internal.shopping-item/ShoppingItem
  [db-wire]
  (when (not-empty db-wire)
    (let [{:keys [category] :as parsed} (-> (misc.datomic/datomic->entity db-wire)
                                            (misc.general/unnamespace-keys))]
      (-> (assoc parsed :category-id (:shopping-category/id category))
          (dissoc :category)))))

