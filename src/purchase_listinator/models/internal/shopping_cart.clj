(ns purchase-listinator.models.internal.shopping-cart
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-item :as models.internal.shopping-item]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]))

#_(def cart-item-skeleton models.internal.shopping-item/shopping-item-skeleton)
#_(s/defschema CartItem cart-item-skeleton)

(s/defschema EventData
  (s/cond-pre
    ))

(def cart-skeleton
  {:moment     s/Num
   :event-type s/Keyword
   :data       EventData})
(s/defschema Cart cart-skeleton)


