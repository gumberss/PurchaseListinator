(ns purchase-listinator.logic.purchase-list
  (:require [schema.core :as s]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.models.internal.purchase-list.purchase-list :as internal.purchase-list]))

(s/defn generate-new :- internal.purchase-list/PurchaseList
  [name :- s/Str
   user-id :- s/Uuid]
  {:id          (misc.general/squuid)
   :enabled     true
   :in-progress false
   :name        name
   :user-id user-id})

(s/defn changed?
  [existent :- internal.purchase-list/PurchaseList
   new :- internal.purchase-list/PurchaseList]
  (not= (:name existent) (:name new)))

(s/defn edit
  [existent :- internal.purchase-list/PurchaseList
   {:keys [name]} :- internal.purchase-list/PurchaseList]
  (assoc existent :name name))

(s/defn enabled?
  [{:keys [enabled]} :- internal.purchase-list/PurchaseList]
  enabled)

(s/defn disabled?
  [purchase-list :- internal.purchase-list/PurchaseList]
  (not (enabled? purchase-list)))

