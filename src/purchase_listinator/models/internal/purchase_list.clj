(ns purchase-listinator.models.internal.purchase-list
  (:require [schema.core :as s]))

(def Status (s/enum :in-progress :waiting))

(def purchase-list-skeleton
  {:id          s/Uuid
   :name        s/Str
   :enabled     s/Bool
   :in-progress s/Bool
   :status      Status})

(s/defschema PurchaseList purchase-list-skeleton)


