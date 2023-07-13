(ns purchase-listinator.modules.shopping-cart.adapters.in.start-shopping
  (:require
    [purchase-listinator.adapters.misc :as adapters.misc]
    [schema.core :as s]
    [purchase-listinator.modules.shopping-cart.schemas.internal.shopping :as schemas.internal.start-shopping]
    [purchase-listinator.modules.shopping-cart.schemas.wire.in.shopping :as schemas.wire.in.start-shopping]))

(s/defn wire->internal :- schemas.internal.start-shopping/StartShopping
  [{:keys [list-id shopping-id]} :- schemas.wire.in.start-shopping/StartShopping]
  {:list-id     (adapters.misc/string->uuid list-id)
   :shopping-id (adapters.misc/string->uuid shopping-id)})