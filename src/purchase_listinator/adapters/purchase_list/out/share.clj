(ns purchase-listinator.adapters.purchase-list.out.share
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list.share :as models.internal.purchase-list.share]
            [purchase-listinator.wires.purchase-list.out.db.share :as wires.purchase-list.out.db.share]))

(s/defn internal->db :- wires.purchase-list.out.db.share/ShareList
  [internal :- models.internal.purchase-list.share/ShareList]
  (purchase-listinator.misc.general/namespace-keys internal "purchase-list-share"))


(s/defn db->internal :- models.internal.purchase-list.share/ShareList
  [{:purchase-list-share/keys [customer-id list-id id]} :- wires.purchase-list.out.db.share/ShareList]
  {:id          id
   :customer-id customer-id
   :list-id     list-id})

