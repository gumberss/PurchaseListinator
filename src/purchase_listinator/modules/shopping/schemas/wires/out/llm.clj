(ns purchase-listinator.modules.shopping.schemas.wires.out.llm
  (:require [schema.core :as s]))

(s/defschema ProductIdentification
  {:id s/Uuid
   :name s/Str})

(s/defschema InteractionRequest
  {:request-id  s/Uuid
   :prompt-name s/Str
   :variables   {:products [ProductIdentification]}
   :images      [s/Str]})
