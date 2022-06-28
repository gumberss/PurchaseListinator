(ns purchase-listinator.adapters.in.purchase-list
  (:require [schema.core :as s])
  (:import (java.util UUID)))

(s/defn wire->internal
  [{:keys [id] :as wire}]
  (assoc wire :id (UUID/fromString id)))