(ns purchase-listinator.publishers.purchase-list
  (:require
    [purchase-listinator.misc.date :as misc.date]
    [purchase-listinator.misc.general :as misc.general]
    [schema.core :as s]
    [purchase-listinator.adapters.purchase-list.out.purchase-list-events :as adapters.purchase-list.out.purchase-list-events]))

(s/defn purchase-list-disabled
  [list-id :- s/Uuid
   user-id :- s/Uuid
   {:keys [publish]}]
  (publish :purchase-listinator/purchase-list.disabled
           (adapters.purchase-list.out.purchase-list-events/->PurchaseListDisabledEvent list-id
                                                                                        user-id
                                                                                        (misc.date/numb-now)
                                                                                        (misc.general/squuid)))
  list-id)