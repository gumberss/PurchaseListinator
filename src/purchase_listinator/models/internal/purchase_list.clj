(ns purchase-listinator.models.internal.purchase-list
  (:require [schema.core :as s]))

(def product-skeleton
  {:id   {:schema s/Uuid :required false}
   :name {:schema s/Str :required true}})
(s/defschema Product product-skeleton)
(s/defschema Products [Product])

(def purchase-list-skeleton
  {:id          {:schema s/Uuid :required false}
   :enabled     {:schema s/Bool :required true}
   :in-progress {:schema s/Bool :required true}
   :products    {:schema Products :required true}})
(s/defschema  PurchaseList purchase-list-skeleton)