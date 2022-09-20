(ns purchase-listinator.adapters.misc
  (:require [schema.core :as s])
  (:import (java.util UUID)))

(s/defn string->uuid :- (s/maybe s/Uuid)
[uuid :- s/Str]
  (when uuid
    (UUID/fromString uuid)))

(s/defn string->integer :- (s/maybe s/Int)
  [number :- s/Num]
  (when number
    (Integer/parseInt number)))
