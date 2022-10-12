(ns purchase-listinator.wires.in.shopping
  (:require [schema.core :as s]))

(def shopping-initiation-skeleton
  {:id      s/Str
   :place   s/Str
   :type    s/Str
   :title   s/Str
   :list-id s/Str
   :latitude s/Num
   :longitude s/Num})
(s/defschema ShoppingInitiation shopping-initiation-skeleton)

(def shopping-initiation-data-request-skeleton
  {:list-id s/Str
   :latitude s/Str
   :longitude s/Str})
(s/defschema ShoppingInitiationDataRequest shopping-initiation-data-request-skeleton)
