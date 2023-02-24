(ns purchase-listinator.wires.out.shopping
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]))

(s/defschema ShoppingFinishedEvent
  {:event-id s/Uuid
   :user-id s/Uuid
   :moment s/Num
   :shopping models.internal.shopping/Shopping})
