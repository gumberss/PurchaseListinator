(ns purchase-listinator.misc.content-type-parser
  (:require [clojure.data.json :as json]
            [camel-snake-kebab.core :as csk]
            [schema.coerce :as coerce]))

(defn transform-content-to
  [body content-type]
  (case content-type
    "text/html" body
    "text/plain" body
    "application/edn" (json/read-str body :key-fn csk/->kebab-case-keyword)
    "application/json" (json/write-str body :key-fn csk/->camelCaseString)))

(defn json->edn
  ([body] (json->edn body nil))
  ([body schema]
   (let [map-data (json/read-str body :key-fn csk/->kebab-case-keyword)
         map-data (if (string? map-data)
                    (json/read-str map-data :key-fn csk/->kebab-case-keyword) ;I don't know why I need to do it the second time
                    map-data)
         coerce-function (if schema
                           (coerce/coercer schema coerce/json-coercion-matcher) identity)]
     (coerce-function map-data))))