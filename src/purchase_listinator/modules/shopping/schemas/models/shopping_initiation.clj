(ns purchase-listinator.modules.shopping.schemas.models.shopping-initiation
  (:require [schema.core :as s]))

(def shopping-initiation-skeleton
  {:id      s/Uuid
   :place   s/Str
   :type    s/Str
   :title   s/Str
   :list-id s/Uuid
   :latitude s/Num
   :longitude s/Num})
(s/defschema ShoppingInitiation shopping-initiation-skeleton)

