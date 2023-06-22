(ns purchase-listinator.adapters.purchase-list.in.share
  (:require
    [purchase-listinator.adapters.misc :as adapters.misc]
    [schema.core :as s]
    [purchase-listinator.models.internal.purchase-list.share :as models.internal.purchase-list.share]
    [purchase-listinator.wires.purchase-list.in.share :as wires.purchase-list.in.share]))

(s/defn wire->internal :- models.internal.purchase-list.share/ShareListRequest
  [{:keys [list-id] :as wire} :- wires.purchase-list.in.share/ShareListRequest]
  (assoc wire :list-id (adapters.misc/string->uuid list-id)))
