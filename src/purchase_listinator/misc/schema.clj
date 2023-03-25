(ns purchase-listinator.misc.schema
  (:require [schema.core :as s]))

(defmacro loose-schema
  [name schema]
  `(s/defschema ~name (assoc ~schema s/Any s/Any)))
