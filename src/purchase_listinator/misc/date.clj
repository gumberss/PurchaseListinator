(ns purchase-listinator.misc.date
  (:require [schema.core :as s]
            [clj-time.core :as t]
            [clj-time.coerce :as c])
  (:import (java.util Date)))

(s/defn numb-now :- s/Num
  []
  (c/to-long (t/now)))

(s/defn numb->date :- Date
  [num :- s/Num]
  (c/to-date num))
