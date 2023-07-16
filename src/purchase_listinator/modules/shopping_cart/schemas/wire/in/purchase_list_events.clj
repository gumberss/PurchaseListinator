(ns purchase-listinator.modules.shopping-cart.schemas.wire.in.purchase-list-events
  (:require [purchase-listinator.misc.schema :as misc.schema]
            [purchase-listinator.modules.shopping-cart.schemas.internal.purchase-list :as modules.shopping-cart.schemas.internal.purchase-list]
            [schema.core :as s]))

(misc.schema/loose-schema ListDisabledEvent
  (-> modules.shopping-cart.schemas.internal.purchase-list/purchase-list-disabled-skeleton
      (assoc :purchase-list-id s/Uuid)
      (dissoc :list-id)))