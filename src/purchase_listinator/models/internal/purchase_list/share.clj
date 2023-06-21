(ns purchase-listinator.models.internal.purchase-list.share
  (:require [schema.core :as s]))

(def share-list-skeleton
  {:list-id           s/Uuid
   :customer-nickname s/Str})

(s/defschema ShareList share-list-skeleton)


