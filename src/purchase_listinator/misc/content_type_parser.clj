(ns purchase-listinator.misc.content-type-parser
  (:require [clojure.data.json :as json]
            [camel-snake-kebab.core :as csk]))

(defn transform-content-to
  [body content-type]
  (case content-type
    "text/html" body
    "text/plain" body
    "application/edn" (json/read-str body :key-fn csk/->kebab-case-keyword)
    "application/json" (json/write-str body :key-fn csk/->camelCaseString)))
