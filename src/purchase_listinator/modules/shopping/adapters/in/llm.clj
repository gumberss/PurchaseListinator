(ns purchase-listinator.modules.shopping.adapters.in.llm
  (:require
    [clojure.data.json :as json]
    [schema.core :as s]
    [purchase-listinator.modules.shopping.schemas.wires.in.llm :as shopping.schemas.wires.in.llm]))

(s/defn wire->model :- [s/Uuid]
  [wire :- shopping.schemas.wires.in.llm/InteractionResponse]
  (when wire
    (-> (:response wire)
        (json/read-str))))