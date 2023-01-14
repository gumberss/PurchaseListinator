(ns purchase-listinator.logic.shopping-category
  (:require [schema.core :as s]
            [purchase-listinator.models.internal.shopping-list :as models.internal.shopping-list]
            [purchase-listinator.models.internal.shopping-category :as models.internal.shopping-category]))


(s/defn ->shopping-category :- models.internal.shopping-category/ShoppingCategory
  [shopping-id :- s/Uuid
   list-category :- models.internal.shopping-list/ShoppingListCategory]
  (-> (assoc list-category :shopping-id shopping-id)
      (dissoc :purchase-list-id)))
