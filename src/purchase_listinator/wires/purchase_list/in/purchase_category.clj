(ns purchase-listinator.wires.purchase-list.in.purchase-category
  (:require [schema.core :as s]))

(def purchase-category-skeleton
  {:name             s/Str
   :id               s/Str
   :order-position   s/Int
   :color            s/Int
   :user-id          s/Uuid
   :purchase-list-id s/Str})
(s/defschema PurchaseCategory purchase-category-skeleton)

(s/defschema PurchaseCategoryCreation
  (dissoc purchase-category-skeleton :order-position :user-id))
