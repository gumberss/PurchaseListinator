(ns purchase-listinator.modules.shopping.adapters.out.llm
  (:require
    [purchase-listinator.modules.shopping.schemas.models.shopping-list :as models.internal.shopping-list]
    [purchase-listinator.modules.shopping.schemas.wires.out.llm :as shopping.schemas.wires.out.llm]
    [schema.core :as s]))

(s/defn ^:private get-items-names+id :- [shopping.schemas.wires.out.llm/ProductIdentification]
  [{:keys [categories]} :- models.internal.shopping-list/ShoppingList]
  (->> (mapcat :items categories)
       (map #(select-keys % [:id :name]))))

(s/defn ->wire-mark-shopping :- shopping.schemas.wires.out.llm/InteractionRequest
  [request-id :- s/Uuid
   shopping :- models.internal.shopping-list/ShoppingList
   image :- s/Str]
  {:request-id  request-id
   :prompt-name "mark_shopping_items"
   :variables   {:products (get-items-names+id shopping)}
   :images      [image]})