(ns purchase-listinator.publishers.pirchase-list-items
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-item :as models.internal.purchase-item]
            [purchase-listinator.adapters.out.purchase-list-item-events :as adapters.out.purchase-list-item-events]
            [purchase-listinator.misc.date :as misc.date]))

(defn a []
  (println ""))
#_(s/defn item-created
  [item :- models.internal.purchase-item/PurchaseItem
   {:keys [publish]}]
  (publish :purchase-listinator/purchase-list.item.created
           (adapters.out.purchase-list-item-events/->ItemCreatedEvent item (misc.date/numb-now))))

#_(s/defn item-changed
  [item :- models.internal.purchase-item/PurchaseItem
   {:keys [publish]}]
  (publish :purchase-listinator/purchase-list.item.changed
           (adapters.out.purchase-list-item-events/->ItemUpdatedEvent item (misc.date/numb-now))))

#_(s/defn item-deleted
  [item :- models.internal.purchase-item/PurchaseItem
   {:keys [publish]}]
  (publish :purchase-listinator/purchase-list.item.deleted
           (adapters.out.purchase-list-item-events/->ItemDeletedEvent item (misc.date/numb-now))))
