(ns purchase-listinator.modules.price-suggestion.components.http
  (:require
    [clj-http.client :as client]
    [com.stuartsierra.component :as component]
    [purchase-listinator.misc.general :as misc.general]
    [purchase-listinator.misc.content-type-parser :as misc.content-type-parser]
    [schema.core :as s]))
(def request-data-skeleton
  {:method                         s/Keyword
   :url                            s/Keyword
   (s/optional-key :content-type)  s/Keyword
   (s/optional-key :params)        [s/Any]
   (s/optional-key :body)          {s/Any s/Any}
   (s/optional-key :result-schema) s/Any})
(s/defschema RequestData request-data-skeleton)

(def request-result-skeleton
  {:status                     s/Int
   :body                       s/Any
   (s/optional-key :exception) s/Any})
(s/defschema RequestResult request-result-skeleton)

(s/defprotocol IHttp
  (request :- RequestResult
    [http :- IHttp
     data :- RequestData]))

(defrecord Http [config]
  component/Lifecycle

  (start [component]
    (assoc component :routes (:price-suggestion/request-routes config)))

  (stop [component]
    (dissoc component :routes))

  IHttp
  (request [{:keys [routes]}
            {:keys [method url params body content-type result-schema headers user-id]}]
    (let [url-str (url routes)
          request-fn (condp = method
                       :get client/get
                       :post client/post
                       :put client/put
                       :delete client/delete)
          request-params (misc.general/assoc-some {:as      "UTF-8"
                                                   :headers (assoc headers :user-id user-id)}
                                                  :params params
                                                  :body body
                                                  :content-type content-type)
          {:keys [body] :as _request-result} (request-fn url-str request-params)
          coerced-data (misc.content-type-parser/json->edn body result-schema)]
      coerced-data)))

(defn new-http []
  (map->Http {}))
