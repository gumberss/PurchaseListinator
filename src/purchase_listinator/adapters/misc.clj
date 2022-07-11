(ns purchase-listinator.adapters.misc
  (:require [schema.core :as s])
  (:import (java.util UUID)))

(s/defn string->uuid :- s/Uuid
[uuid :- s/Str]
  (UUID/fromString uuid))