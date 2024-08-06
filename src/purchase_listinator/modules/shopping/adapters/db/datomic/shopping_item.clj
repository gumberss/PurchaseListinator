(ns purchase-listinator.modules.shopping.adapters.db.datomic.shopping-item
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.modules.shopping.schemas.models.shopping-item :as models.internal.shopping-item]))

(s/defn internal->db
  [{:keys [category-id order-position price] :as internal} :- models.internal.shopping-item/ShoppingItem]
  (-> (dissoc internal :category-id)
      (assoc :category {:shopping-category/id category-id}
             :order-position (long order-position)
             :price (float price))
      (misc.general/namespace-keys :shopping-item)))

(s/defn db->internal :- models.internal.shopping-item/ShoppingItem
  [db-wire]
  (when (not-empty db-wire)
    (let [{:keys [category] :as parsed} (-> (misc.datomic/datomic->entity db-wire)
                                            (misc.general/unnamespace-keys))]
      (-> (assoc parsed :category-id (:shopping-category/id category))
          (dissoc :category)))))

