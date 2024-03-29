(ns purchase-listinator.models.internal.purchase-list.purchase-category
  (:require [schema.core :as s]))

(def purchase-category-skeleton
  {:name             s/Str
   :id               s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid
   :user-id          s/Uuid})
(s/defschema PurchaseCategory purchase-category-skeleton)

(s/defschema PurchaseCategoryCreation
  (-> (dissoc purchase-category-skeleton :order-position :user-id)
      (assoc (s/optional-key :user-id) s/Uuid
             (s/optional-key :order-position) s/Int)))
