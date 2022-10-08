(ns purchase-listinator.misc.date
  (:require [schema.core :as s]
            [clj-time.core :as t]
            [clj-time.coerce :as c]))

(s/defn numb-now :- s/Num
  []
  (c/to-long (t/now)))
