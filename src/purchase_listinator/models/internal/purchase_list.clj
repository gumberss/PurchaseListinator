(ns purchase-listinator.models.internal.purchase-list
  (:require [schema.core :as s]))

(def product-skeleton
  {:id   s/Uuid
   :name s/Str})
(s/defschema Product product-skeleton)
(s/defschema Products [Product])

(def purchase-list-skeleton
  {:id          s/Uuid
   :name        s/Str
   :enabled     s/Bool
   :in-progress s/Bool
   :products    Products})
(s/defschema PurchaseList purchase-list-skeleton)


