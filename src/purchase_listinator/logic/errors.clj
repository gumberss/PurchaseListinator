(ns purchase-listinator.logic.errors
  (:require [schema.core :as s]
            [cats.monad.either :refer [left]]))

(s/defn build
  [status :- s/Int
   error :- {:message s/Str}]
  {:status status
   :error  error})

(s/defn build-left
  [status :- s/Int
   error :- {:message s/Str}]
  (left {:status status
         :error  error}))