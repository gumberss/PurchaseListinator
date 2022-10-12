(ns purchase-listinator.models.internal.shopping-initiation-data-request
  (:require [schema.core :as s]))

(def shopping-initiation-data-request-skeleton
  {:list-id   s/Uuid
   :latitude  s/Num
   :longitude s/Num})
(s/defschema ShoppingInitiationDataRequest shopping-initiation-data-request-skeleton)
