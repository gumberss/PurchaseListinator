(ns purchase-listinator.models.internal.shopping-management-data
  (:require [schema.core :as s]))

(def management-item-skeleton
  {:id             s/Uuid
   :name           s/Str
   :quantity       s/Int
   :order-position s/Int
   :category-id    s/Uuid})
(s/defschema ManagementItem management-item-skeleton)

(def management-categories-skeleton
  {:name             s/Str
   :id               s/Uuid
   :order-position   s/Int
   :color            s/Int
   :purchase-list-id s/Uuid
   :items [ManagementItem]})
(s/defschema ManagementCategory management-categories-skeleton)

(def purchase-list-management-data-skeleton
  {:purchase-list-id s/Uuid
   :categories [ManagementCategory]})
(s/defschema ManagementData purchase-list-management-data-skeleton)

