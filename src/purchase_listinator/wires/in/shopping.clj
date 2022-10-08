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

