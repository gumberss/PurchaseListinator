(ns purchase-listinator.adapters.db.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]))


(s/defn internal->db [internal]
  (-> internal
      (misc.general/namespace-keys :purchase-list)))

(s/defn db->internal [db]
  (misc.general/unnamespace-keys db))