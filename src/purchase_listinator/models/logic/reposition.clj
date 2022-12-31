(ns purchase-listinator.models.logic.reposition
  (:require [schema.core :as s]))

(def reorder-skeleton
  {:order-position   s/Int
   s/Any s/Any})

(s/defschema Reorder reorder-skeleton)
