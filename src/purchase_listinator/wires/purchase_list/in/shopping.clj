(ns purchase-listinator.wires.purchase-list.in.shopping
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.purchase-list.shopping :as models.internal.purchase-list.shopping]))

(s/defschema ShoppingFinishedEvent
  {:event-id s/Uuid
   :moment s/Num
   :shopping models.internal.purchase-list.shopping/Shopping})
