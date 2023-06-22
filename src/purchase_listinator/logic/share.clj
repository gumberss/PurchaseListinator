(ns purchase-listinator.logic.share
  (:require
    [purchase-listinator.models.internal.purchase-list.share :as models.internal.purchase-list.share]
    [schema.core :as s]))

(s/defn ->share-list :- models.internal.purchase-list.share/ShareList
  [{:keys [list-id]} :- models.internal.purchase-list.share/ShareListRequest
   customer-id :- s/Uuid]
  {:list-id     list-id
   :customer-id customer-id})