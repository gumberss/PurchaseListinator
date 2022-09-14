(ns purchase-listinator.models.internal.purchase-category
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-item :as models.internal.purchase-item]))

(def purchase-category-skeleton
  {:name             s/Str
   :id               s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid
   :purchase-items   models.internal.purchase-item/PurchaseItems})
(s/defschema PurchaseCategory purchase-category-skeleton)
(s/defschema PurchaseCategories [PurchaseCategory])
