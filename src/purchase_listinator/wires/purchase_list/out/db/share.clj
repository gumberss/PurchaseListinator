(ns purchase-listinator.wires.purchase-list.out.db.share
  (:require [schema.core :as s]))

(def share-list-skeleton
  {:purchase-list-share/list-id           s/Uuid
   :purchase-list-share/customer-nickname s/Str})

(s/defschema ShareList share-list-skeleton)


