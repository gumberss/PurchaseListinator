(ns purchase-listinator.misc.pedestal
  (:require  [clojure.data.json :as json]))

(defn accepted-type
  [context]
  (get-in context [:request :accept :field] "application/json"))

(defn transform-content
  [body content-type]
  (case content-type
    "text/html"        body
    "text/plain"       body
    "application/edn"  (pr-str body)
    "application/json" (json/write-str body)))

(defn coerce-to
  [response content-type]
  (-> response
      (update :body transform-content content-type)
      (assoc-in [:headers "Content-Type"] content-type)))

(def coerce-body
  {:name ::coerce-body
   :leave
   (fn [context]
     (cond-> context
             (nil? (get-in context [:response :headers "Content-Type"]))                    ;; <1>
             (update-in [:response] coerce-to (accepted-type context))))})