(ns purchase-listinator.modules.shopping.diplomat.publishers.shopping
  (:require
    [purchase-listinator.components.rabbitmq-channel :as components.rabbitmq-channel]
    [schema.core :as s]
    [purchase-listinator.misc.date :as misc.date]
    [purchase-listinator.misc.general :as misc.general]
    [purchase-listinator.modules.shopping.adapters.out.shopping :as adapters.out.shopping]
    [purchase-listinator.modules.shopping.schemas.models.shopping :as models.internal.shopping]))

(s/defn shopping-finished :- models.internal.shopping/Shopping
  [shopping :- models.internal.shopping/Shopping
   producer :- components.rabbitmq-channel/IProducer]
  (components.rabbitmq-channel/publish producer
                                       :purchase-listinator/shopping.finished
                                       (adapters.out.shopping/->ShoppingFinishedEvent shopping (misc.date/numb-now) (misc.general/squuid)))
  shopping)
