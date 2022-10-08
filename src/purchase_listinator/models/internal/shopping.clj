(ns purchase-listinator.models.internal.shopping
  (:require [schema.core :as s]))

(def shopping-skeleton
  {:id       s/Uuid
   :place    s/Str
   :type     s/Str
   :title    s/Str
   :date     s/Num
   :list-id  s/Uuid
   :duration s/Num})
(s/defschema Shopping shopping-skeleton)

