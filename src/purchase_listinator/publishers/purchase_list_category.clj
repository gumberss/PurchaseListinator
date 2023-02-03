(ns purchase-listinator.publishers.purchase-list-category
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list.purchase-category :as models.internal.purchase-category]
            [purchase-listinator.adapters.out.purchase-list-category-events :as adapters.out.purchase-list-category-events]
            [purchase-listinator.misc.date :as misc.date]
            [purchase-listinator.misc.general :as misc.general]))

(s/defn category-deleted
  [category :- models.internal.purchase-category/PurchaseCategory
   {:keys [publish]}]
  (publish :purchase-listinator/purchase-list.category.deleted
           (adapters.out.purchase-list-category-events/->CategoryDeletedEvent category (misc.date/numb-now) (misc.general/squuid)))
  category)

(s/defn category-created
  [category :- models.internal.purchase-category/PurchaseCategory
   {:keys [publish]}]
  (publish :purchase-listinator/purchase-list.category.created
           (adapters.out.purchase-list-category-events/->CategoryCreatedEvent category (misc.date/numb-now) (misc.general/squuid)))
  category)

