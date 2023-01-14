(ns purchase-listinator.publishers.shopping
  (:require [schema.core :as s]
            [purchase-listinator.adapters.out.shopping :as adapters.out.shopping]
            [purchase-listinator.misc.date :as misc.date]
            [purchase-listinator.misc.general :as misc.general]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]))

(s/defn shopping-finished :- models.internal.shopping/Shopping
  [shopping :- models.internal.shopping/Shopping
   {:keys [publish]}]
  (publish :purchase-listinator/shopping.finished
           (adapters.out.shopping/->ShoppingFinishedEvent shopping (misc.date/numb-now) (misc.general/squuid)))
  shopping)
