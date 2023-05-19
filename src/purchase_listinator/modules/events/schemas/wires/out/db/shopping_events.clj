(ns purchase-listinator.modules.events.schemas.wires.out.db.shopping-events
  (:require
    [schema.core :as s]))

(def shopping-event-skeleton
  {:shopping-event/id                           s/Uuid
   :shopping-event/moment                       s/Num
   :shopping-event/event-type                   s/Keyword
   :shopping-event/user-id                      s/Uuid
   :shopping-event/shopping-id                  s/Uuid
   (s/optional-key :shopping-event/item-id)     s/Uuid
   (s/optional-key :shopping-event/category-id) s/Uuid
   :shopping-event/properties                   s/Str})
(s/defschema ShoppingEvent
  shopping-event-skeleton)

(s/defschema ShoppingEventDb
  (assoc shopping-event-skeleton :db/id s/Int))