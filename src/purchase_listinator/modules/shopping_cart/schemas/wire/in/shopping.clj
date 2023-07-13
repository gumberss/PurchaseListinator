(ns purchase-listinator.modules.shopping-cart.schemas.wire.in.shopping
  (:require [purchase-listinator.misc.schema :as misc.schema]
            [purchase-listinator.modules.shopping-cart.schemas.internal.shopping :as internal.shopping]
            [schema.core :as s]))

(def start-shopping-skeleton
  {:list-id     s/Str
   :shopping-id s/Str})
(misc.schema/loose-schema StartShopping
  start-shopping-skeleton)

(misc.schema/loose-schema Shopping
  internal.shopping/shopping-skeleton)
(misc.schema/loose-schema CloseShoppingEvent
  {:shopping Shopping})