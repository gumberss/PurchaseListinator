(ns purchase-listinator.modules.shopping.schemas.models.shopping-location
  (:require [schema.core :as s]))

(def shopping-location
  {:id          s/Uuid
   :latitude    s/Str
   :longitude   s/Str
   :shopping-id s/Uuid})
(s/defschema ShoppingLocation shopping-location)


