(ns purchase-listinator.misc.either
  (:require [cats.monad.either :refer [left right left? either? branch]]))

(defmacro try-right
  [function]
  `(try (let [result# ~function]
          (if (either? result#)
            result#
            (right result#)))
        (catch Exception e#
          (println "[[OUT_ERROR]]")
          (println e#)
          (left e#))))
