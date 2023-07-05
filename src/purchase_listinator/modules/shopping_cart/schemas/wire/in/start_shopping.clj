(ns purchase-listinator.modules.shopping-cart.schemas.wire.in.start-shopping
  (:require [purchase-listinator.misc.schema :as misc.schema]
            [schema.core :as s]))

(def start-shopping-skeleton
  {:list-id s/Str})

(misc.schema/loose-schema StartShopping
  start-shopping-skeleton)