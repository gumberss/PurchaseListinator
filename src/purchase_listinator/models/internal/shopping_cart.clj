(ns purchase-listinator.models.internal.shopping-cart
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-item :as models.internal.shopping-item]
            [purchase-listinator.models.internal.shopping :as models.internal.shopping]))

(def cart-item-skeleton models.internal.shopping-item/shopping-item-skeleton)
(s/defschema CartItem cart-item-skeleton)

(def cart-skeleton
  (assoc models.internal.shopping/shopping-skeleton
    :items [CartItem]))
(s/defschema Cart cart-skeleton)


