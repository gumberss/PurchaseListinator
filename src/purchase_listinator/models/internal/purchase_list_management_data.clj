(ns purchase-listinator.models.internal.purchase-list-management-data
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]))

(def purchase-list-management-data-skeleton
  {:purchase-list-id s/Uuid
   :categories models.internal.purchase-category/PurchaseCategories})
(s/defschema ManagementData purchase-list-management-data-skeleton)
