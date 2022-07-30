(ns purchase-listinator.misc.either
  (:require [cats.monad.either :refer [left right either?]]))

(defmacro try-right
  [function]
  `(try (let [result# ~function]
          (if (either? result#)
            result#
            (right result#)))
        (catch Exception e#
          (println e#)
          (left e#))))