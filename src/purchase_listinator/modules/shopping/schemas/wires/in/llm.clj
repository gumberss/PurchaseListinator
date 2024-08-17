(ns purchase-listinator.modules.shopping.schemas.wires.in.llm
  (:require [schema.core :as s]))

(s/defschema InteractionResponse
  {:request-id s/Uuid
   :response (s/maybe s/Str)
   :status s/Str
   :details s/Str})