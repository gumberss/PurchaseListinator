(ns purchase-listinator.logic.user
  (:require [schema.core :as s]))

(def limit-characters 50)

(s/defn valid-nickname?
  [nickname :- s/Str]
  (and (not-empty nickname)
      (>= limit-characters (count nickname))))
