(ns purchase-listinator.modules.shopping.schemas.wires.out.shopping
  (:require
    [schema.core :as s]
    [purchase-listinator.modules.shopping.schemas.models.shopping :as models.shopping]))

(s/defschema ShoppingFinishedEvent
  {:event-id s/Uuid
   :user-id  s/Uuid
   :moment   s/Num
   :shopping models.shopping/Shopping})
