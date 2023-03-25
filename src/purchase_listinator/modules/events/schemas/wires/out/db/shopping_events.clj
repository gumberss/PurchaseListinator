(ns purchase-listinator.modules.events.schemas.wires.out.db.shopping-events
  (:require [schema.core :as s]))

(s/defschema ShoppingEvent
             {:id          s/Uuid
              :moment      s/Num
              :event-type  s/Keyword
              :user-id     s/Uuid
              :shopping-id s/Uuid
              :properties  s/Str})
