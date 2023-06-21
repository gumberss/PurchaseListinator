(ns purchase-listinator.adapters.purchase-list.out.share
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list.share :as models.internal.purchase-list.share]
            [purchase-listinator.wires.purchase-list.out.db.share :as wires.purchase-list.out.db.share]))

(s/defn internal->db :- wires.purchase-list.out.db.share/ShareList
  [internal :- models.internal.purchase-list.share/ShareList]
  (purchase-listinator.misc.general/namespace-keys internal "purchase-list-share"))

