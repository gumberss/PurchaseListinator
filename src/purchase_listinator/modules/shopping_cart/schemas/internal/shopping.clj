(ns purchase-listinator.modules.shopping-cart.schemas.internal.shopping
  (:require [schema.core :as s]))

(def start-shopping-skeleton
  {:list-id     s/Uuid
   :shopping-id s/Uuid})

(s/defschema StartShopping
  start-shopping-skeleton)

(def shopping-skeleton
  {:id      s/Uuid
   :list-id s/Uuid})
(s/defschema Shopping shopping-skeleton)

(def close-shopping-skeleton
  {:shopping Shopping})
(s/defschema CloseShopping close-shopping-skeleton)