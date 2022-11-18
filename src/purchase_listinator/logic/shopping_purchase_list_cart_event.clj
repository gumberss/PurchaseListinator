(ns purchase-listinator.logic.shopping-purchase-list-cart-event
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-cart :as models.internal.shopping-cart]))

(s/defn created->category
  [{:keys [category-id] :as event} :- models.internal.shopping-cart/PurchaseListCategoryCreated]
  (-> event
      (assoc :id category-id
             :items [])
      (dissoc :category-id)))
