(ns purchase-listinator.misc.schema
  (:require [schema.utils :as utils]
            [schema.coerce :as coerce]
            [schema.core :as s])
  (:import (clojure.lang PersistentHashMap PersistentArrayMap)))

(defmacro loose-schema
  [name schema]
  `(s/defschema ~name (assoc ~schema s/Any s/Any)))
