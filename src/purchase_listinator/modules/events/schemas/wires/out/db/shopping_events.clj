(ns purchase-listinator.modules.events.schemas.wires.out.db.shopping-events
  (:require
    [purchase-listinator.misc.general :as misc.general]
    [schema.core :as s]))

(s/defschema ShoppingEvent
  {:shopping-event/id                           s/Uuid
   :shopping-event/moment                       s/Num
   :shopping-event/event-type                   s/Keyword
   :shopping-event/user-id                      s/Uuid
   :shopping-event/shopping-id                  s/Uuid
   (s/optional-key :shopping-event/item-id)     s/Uuid
   (s/optional-key :shopping-event/category-id) s/Uuid
   :shopping-event/properties                   s/Str})
