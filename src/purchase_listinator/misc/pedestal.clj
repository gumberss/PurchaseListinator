(ns purchase-listinator.misc.pedestal
  (:require [purchase-listinator.misc.content-type-parser :as misc.content-type-parser]
            [io.pedestal.interceptor.error :as pedestal.interceptor.error]
            [schema.coerce :as coerce]))

(defn accepted-type
  [context]
  (get-in context [:request :accept :field] "application/json"))

(defn coerce-response-to
  [response content-type]
  (-> response
      (update :body misc.content-type-parser/transform-content-to content-type)
      (assoc-in [:headers "Content-Type"] content-type)))

(def coerce-body-content-type
  {:name ::coerce-body
   :leave
   (fn [context]
     (cond-> context
             (nil? (get-in context [:response :headers "Content-Type"]))
             (update-in [:response] coerce-response-to (accepted-type context))))})


(def service-error-handler
  (pedestal.interceptor.error/error-dispatch [ctx ex]
    :else (do (clojure.pprint/pprint ex)
              (assoc ctx :io.pedestal.interceptor.chain/error ex))))
