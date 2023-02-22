(ns purchase-listinator.adapters.db.user
  (:require
    [purchase-listinator.misc.general :as misc.general]
    [schema.core :as s]))

(s/defn internal->db
  [user]
  (misc.general/namespace-keys user :user))

(s/defn db->internal
  [wire]
  (misc.general/unnamespace-keys wire))