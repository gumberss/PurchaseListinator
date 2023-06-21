(ns purchase-listinator.adapters.purchase-list.out.share
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list.share :as models.internal.purchase-list.share]))

(s/defn internal->db
  [internal :- models.internal.purchase-list.share/ShareList]
  (purchase-listinator.misc.general/namespace-keys internal "purchase-list-share"))

