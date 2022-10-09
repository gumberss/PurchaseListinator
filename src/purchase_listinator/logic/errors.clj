(ns purchase-listinator.logic.errors
  (:require [schema.core :as s]))

(s/defn build
        [status :- s/Int
         error :- s/Str]
        {:status status
         :error error})
