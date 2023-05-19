(ns purchase-listinator.wires.out.shopping
  (:require
    [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]
    [schema.core :as s]
    [purchase-listinator.models.internal.shopping :as models.internal.shopping]))

(s/defschema ShoppingFinishedEvent
  {:event-id s/Uuid
   :user-id  s/Uuid
   :moment   s/Num
   :shopping models.internal.shopping/Shopping
   :events   [models.internal.shopping-cart/CartEvent]})
