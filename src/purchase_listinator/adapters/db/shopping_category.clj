(ns purchase-listinator.adapters.db.shopping-category
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]
            [purchase-listinator.models.internal.shopping-category :as models.internal.shopping-category]))


(s/defn internal->db
  [{:keys [shopping-id order-position] :as internal} :- models.internal.shopping-category/ShoppingCategory]
  (-> (dissoc internal :shopping-id :items)
      (assoc :shopping {:shopping/id shopping-id}
             :order-position (long order-position))
      (misc.general/namespace-keys :shopping-category)))

(s/defn db->internal :- models.internal.shopping-category/ShoppingCategory
  [db-wire]
  (when (not-empty db-wire)
    (let [{:keys [shopping] :as internal} (-> (misc.datomic/datomic->entity db-wire)
                                              (misc.general/unnamespace-keys))]
      (-> (assoc internal :shopping-id (:shopping/id shopping))
          (dissoc :shopping)))))

