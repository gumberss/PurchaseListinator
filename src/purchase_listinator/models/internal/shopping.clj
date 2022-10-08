(ns purchase-listinator.models.internal.shopping
  (:require [schema.core :as s]))

(def status (s/enum :in-progress :done :canceled))
(s/defschema Status status)

(def shopping-skeleton
  {:id       s/Uuid
   :place    s/Str
   :type     s/Str
   :title    s/Str
   :date     s/Num
   :list-id  s/Uuid
   :status   Status
   (s/optional-key :duration) s/Num})
(s/defschema Shopping shopping-skeleton)

