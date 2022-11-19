(ns purchase-listinator.publishers.purchase-list-category
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.adapters.out.purchase-list-category-events :as adapters.out.purchase-list-category-events]
            [purchase-listinator.misc.date :as misc.date]))

(s/defn category-deleted
  [category :- models.internal.purchase-category/PurchaseCategory
   {:keys [publish]}]
  (publish :purchase-listinator/purchase-list.category.deleted
           (adapters.out.purchase-list-category-events/->CategoryDeletedEvent category (misc.date/numb-now))))

(s/defn category-created
  [category :- models.internal.purchase-category/PurchaseCategory
   {:keys [publish]}]
  (publish :purchase-listinator/purchase-list.category.created
           (adapters.out.purchase-list-category-events/->CategoryCreatedEvent category (misc.date/numb-now))))

