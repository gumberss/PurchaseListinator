(ns purchase-listinator.models.internal.purchase-list.share
  (:require [schema.core :as s]))

(def share-list-request-skeleton
  {:list-id           s/Uuid
   :customer-nickname s/Str})
(s/defschema ShareListRequest share-list-request-skeleton)

(def share-list-skeleton
  {:id          s/Uuid
   :list-id     s/Uuid
   :customer-id s/Uuid})
(s/defschema ShareList share-list-skeleton)
