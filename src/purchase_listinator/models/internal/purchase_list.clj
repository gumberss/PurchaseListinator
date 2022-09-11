(ns purchase-listinator.models.internal.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]))

(def purchase-list-skeleton
  {:id          s/Uuid
   :name        s/Str
   :enabled     s/Bool
   :in-progress s/Bool
   :categories  models.internal.purchase-category/PurchaseCategories})
(s/defschema PurchaseList purchase-list-skeleton)


