(ns purchase-listinator.modules.shopping-cart.adapters.in.start-shopping
  (:require
    [purchase-listinator.adapters.misc :as adapters.misc]
    [schema.core :as s]
    [purchase-listinator.modules.shopping-cart.schemas.internal.start-shopping :as schemas.internal.start-shopping]
    [purchase-listinator.modules.shopping-cart.schemas.wire.in.start-shopping :as schemas.wire.in.start-shopping]))

;todo: tests
(s/defn wire->internal :- schemas.internal.start-shopping/StartShopping
  [{:keys [list-id]} :- schemas.wire.in.start-shopping/StartShopping]
  {:list-id (adapters.misc/string->uuid list-id)})