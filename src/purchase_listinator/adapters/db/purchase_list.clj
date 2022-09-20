(ns purchase-listinator.adapters.db.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.misc.datomic :as misc.datomic]))


(s/defn add-category->db [list-id
                          internal-category]
  {:purchase-list/id                  list-id
   :purchase-list/purchase-categories [(purchase-listinator.adapters.db.purchase-category/internal->db internal-category)]})

(s/defn internal->db [internal]
  (-> internal
      (misc.general/namespace-keys :purchase-list)))

(s/defn db->internal [db]
  (-> (misc.datomic/datomic->entity db)
      (misc.general/unnamespace-keys)))
