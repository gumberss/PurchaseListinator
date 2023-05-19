(ns purchase-listinator.modules.events.schemas.wires.in.shopping-events
  (:require [purchase-listinator.misc.schema :as misc.schema]
            [schema.core :as s]
            [purchase-listinator.misc.schema :as misc.schema]))
(misc.schema/loose-schema Event
  {:id                           s/Uuid
   :moment                       s/Num
   :event-type                   s/Keyword
   :user-id                      s/Uuid
   :shopping-id                  s/Uuid
   (s/optional-key :item-id)     s/Uuid
   (s/optional-key :category-id) s/Uuid})