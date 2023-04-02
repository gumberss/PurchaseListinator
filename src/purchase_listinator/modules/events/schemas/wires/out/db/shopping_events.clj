(ns purchase-listinator.modules.events.schemas.wires.out.db.shopping-events
  (:require
    [purchase-listinator.misc.general :as misc.general]
    [schema.core :as s]))

(s/defschema ShoppingEvent
  (-> {:id                           s/Uuid
       :moment                       s/Num
       :event-type                   s/Keyword
       :user-id                      s/Uuid
       :shopping-id                  s/Uuid
       (s/optional-key :item-id)     s/Uuid
       (s/optional-key :category-id) s/Uuid
       :properties                   s/Str}
      (misc.general/namespace-keys "shopping-event")))
