(ns purchase-listinator.modules.shopping-cart.schemas.internal.start-shopping
  (:require [schema.core :as s]))

(def start-shopping-skeleton
  {:list-id s/Uuid})

(s/defschema StartShopping
  start-shopping-skeleton)