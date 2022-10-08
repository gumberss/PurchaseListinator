(ns purchase-listinator.misc.http
  (:require [schema.core :as s]
            [cats.monad.either :refer :all]))

(s/defn ->Error
        [{:keys [status error] :as err}]
        (println err)
        {:status (or status 500)
         :body   (or error err)})

(s/defn ->Success
        [data]
        (if (left? data)
          (->Error data)
          {:status 200
           :body   data}))

(s/defn default-branch*
        [err-func
         suc-fun
         try-fun]
        (branch try-fun err-func suc-fun))

(s/defn default-branch
        [try-fun]
        (default-branch* ->Error ->Success try-fun))
