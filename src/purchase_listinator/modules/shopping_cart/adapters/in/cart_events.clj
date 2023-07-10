(ns purchase-listinator.modules.shopping-cart.adapters.in.cart-events
  (:require
    [purchase-listinator.adapters.misc :as adapters.misc]
    [schema.core :as s]
    [purchase-listinator.modules.shopping-cart.schemas.internal.cart-events :as internal.cart-events]
    [purchase-listinator.modules.shopping-cart.schemas.wire.in.shopping-events :as wire.in.shopping-events]))


(defmulti wire->internal (fn [{:keys [event-type]} _ _] (keyword event-type)))

(s/defmethod wire->internal :reorder-category :- internal.cart-events/ReorderCategoryEvent
  [{:keys [event-type shopping-id category-id event-id] :as wire} :- wire.in.shopping-events/ReorderCategoryEvent
   moment :- s/Num
   user-id :- s/Uuid]
  (-> (assoc wire :event-type (keyword event-type)
                  :shopping-id (adapters.misc/string->uuid shopping-id)
                  :category-id (adapters.misc/string->uuid category-id)
                  :id (adapters.misc/string->uuid event-id)
                  :moment moment
                  :user-id user-id)
      (dissoc :event-id)))

(s/defmethod wire->internal :reorder-item :- internal.cart-events/ReorderItemEvent
  [{:keys [event-type shopping-id new-category-id item-id event-id] :as wire} :- wire.in.shopping-events/ReorderItemEvent
   moment :- s/Num
   user-id :- s/Uuid]
  (-> (assoc wire :event-type (keyword event-type)
                  :shopping-id (adapters.misc/string->uuid shopping-id)
                  :item-id (adapters.misc/string->uuid item-id)
                  :new-category-id (adapters.misc/string->uuid new-category-id)
                  :id (adapters.misc/string->uuid event-id)
                  :moment moment
                  :user-id user-id)
      (dissoc :event-id)))

(s/defmethod wire->internal :change-item :- internal.cart-events/ChangeItemEvent
  [{:keys [event-type shopping-id item-id event-id] :as wire} :- wire.in.shopping-events/ChangeItemEvent
   moment :- s/Num
   user-id :- s/Uuid]
  (-> (assoc wire :event-type (keyword event-type)
                  :shopping-id (adapters.misc/string->uuid shopping-id)
                  :item-id (adapters.misc/string->uuid item-id)
                  :id (adapters.misc/string->uuid event-id)
                  :moment moment
                  :user-id user-id)
      (dissoc :event-id)))