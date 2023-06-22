(ns purchase-listinator.wires.purchase-list.in.share
  (:require [schema.core :as s]))

(def share-list-skeleton
  {:list-id     s/Str
   :customer-nickname s/Str})

(s/defschema ShareListRequest share-list-skeleton)

