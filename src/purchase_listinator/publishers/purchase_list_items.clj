(ns purchase-listinator.publishers.purchase-list-items
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list.purchase-item :as models.internal.purchase-item]
            [purchase-listinator.adapters.out.purchase-list-item-events :as adapters.out.purchase-list-item-events]
            [purchase-listinator.misc.date :as misc.date]
            [purchase-listinator.wires.purchase-list.out.purchase-list-item-events :as wires.out.purchase-list-item-events]
            [purchase-listinator.misc.general :as misc.general]))


(s/defn item-created :- models.internal.purchase-item/PurchaseItem
  [item :- models.internal.purchase-item/PurchaseItem
   {:keys [publish]}]
  (publish :purchase-listinator/purchase-list.item.created
           (adapters.out.purchase-list-item-events/->ItemCreatedEvent item (misc.date/numb-now) (misc.general/squuid)))
  item)

(s/defn item-changed :- models.internal.purchase-item/PurchaseItem
  [item :- models.internal.purchase-item/PurchaseItem
   {:keys [publish]}]
  (publish :purchase-listinator/purchase-list.item.changed
           (adapters.out.purchase-list-item-events/->ItemChangedEvent item (misc.date/numb-now) (misc.general/squuid)))
  item)

(s/defn item-deleted :- models.internal.purchase-item/PurchaseItem
  [item :- models.internal.purchase-item/PurchaseItem
   {:keys [publish]}]
  (publish :purchase-listinator/purchase-list.item.deleted
           (adapters.out.purchase-list-item-events/->ItemDeletedEvent item (misc.date/numb-now) (misc.general/squuid)))
  item)
