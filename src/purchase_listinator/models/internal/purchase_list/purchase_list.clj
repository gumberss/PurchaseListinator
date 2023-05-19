(ns purchase-listinator.models.internal.purchase-list.purchase-list
  (:require [schema.core :as s]))

(def Status (s/enum :in-progress :waiting))

(def purchase-list-skeleton
  {:id          s/Uuid
   :name        s/Str
   :user-id     s/Uuid
   :enabled     s/Bool
   :in-progress s/Bool})

(s/defschema PurchaseList purchase-list-skeleton)


