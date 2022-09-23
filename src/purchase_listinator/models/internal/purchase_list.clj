(ns purchase-listinator.models.internal.purchase-list
  (:require [schema.core :as s]))

(def purchase-list-skeleton
  {:id          s/Uuid
   :name        s/Str
   :enabled     s/Bool
   :in-progress s/Bool})
(s/defschema PurchaseList purchase-list-skeleton)


