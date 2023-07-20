(ns purchase-listinator.wires.in.shopping-cart
  (:require
    [purchase-listinator.misc.schema :as misc.schema]
    [purchase-listinator.models.internal.cart-events :as internal.cart-events]
    [purchase-listinator.models.internal.shopping.purchase-list :as internal.shopping.purchase-list]
    [schema.core :as s]))

(s/defschema ReorderCategoryEvent
  {:event-id     s/Str
   :event-type   s/Str
   :shopping-id  s/Str
   :category-id  s/Str
   :new-position s/Int})

(s/defschema ReorderItemEvent
  {:event-id        s/Str
   :event-type      s/Str
   :shopping-id     s/Str
   :item-id         s/Str
   :new-position    s/Int
   :new-category-id s/Str})

(s/defschema ChangeItemEvent
  {:event-id         s/Str
   :event-type       s/Str
   :shopping-id      s/Str
   :item-id          s/Str
   :price            s/Num
   :quantity-changed s/Int})

(misc.schema/loose-schema Cart
  {:purchase-list        internal.shopping.purchase-list/PurchaseList
   :shopping-cart-events [internal.cart-events/CartEvent]})
