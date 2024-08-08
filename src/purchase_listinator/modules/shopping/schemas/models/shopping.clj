(ns purchase-listinator.modules.shopping.schemas.models.shopping
  (:require [schema.core :as s]
            [purchase-listinator.modules.shopping.schemas.models.shopping-category :as models.internal.shopping-category]))

(def status (s/enum :in-progress :done :canceled))
(s/defschema Status status)

(def shopping-skeleton
  {:id                          s/Uuid
   :place                       s/Str
   :type                        s/Str
   :title                       s/Str
   :date                        s/Num
   :list-id                     s/Uuid
   :status                      Status
   (s/optional-key :duration)   s/Num
   (s/optional-key :categories) [models.internal.shopping-category/ShoppingCategory]})
(s/defschema Shopping shopping-skeleton)

