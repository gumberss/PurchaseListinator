(ns purchase-listinator.components.http
  (:require
    [clj-http.client :as client]
    [clojure.string :as str]
    [com.stuartsierra.component :as component]
    [purchase-listinator.misc.general :as misc.general]
    [purchase-listinator.misc.content-type-parser :as misc.content-type-parser]
    [schema.core :as s]))
(def request-data-skeleton
  {:method                         s/Keyword
   :url                            s/Keyword
   (s/optional-key :content-type)  s/Keyword
   (s/optional-key :query-params)  s/Any
   (s/optional-key :body)          {s/Any s/Any}
   (s/optional-key :result-schema) s/Any})
(s/defschema RequestData request-data-skeleton)

(def request-result-skeleton
  {:status                     s/Int
   :body                       s/Any
   (s/optional-key :exception) s/Any})
(s/defschema RequestResult request-result-skeleton)

(defprotocol IHttp
  (request
    [http
     data]))

(defn modify-keys [f m]
  (zipmap (map f (keys m)) (vals m)))

(defn modify-vals [f m]
  (zipmap (keys m) (map f (vals m))))

(defn ->path-param
  [value]
  (cond
    (uuid? value) (str value)
    :else value))

(s/defn replace-variable
  [url [key val]]
  (if (str/includes? url key)
    (str/replace url key val)
    url))
(s/defn fill-path-params [url params]
  (->> (modify-keys str params)
       (modify-vals ->path-param)
       (reduce replace-variable url)))

(defrecord Http [config request-routes-key]
  component/Lifecycle

  (start [component]
    (assoc component :routes (request-routes-key config)))

  (stop [component]
    (dissoc component :routes))

  IHttp
  (request [{:keys [routes]}
            {:keys [method url query-params body content-type result-schema headers user-id]}]
    (let [url-str (-> (url routes)
                      (fill-path-params query-params))
          content-type (or content-type "application/json")
          request-fn (condp = method
                       :get client/get
                       :post client/post
                       :put client/put
                       :delete client/delete)
          request-params (misc.general/assoc-some {:as      "UTF-8"
                                                   :headers (assoc headers :user-id user-id)}
                                                  :query-params query-params
                                                  :body (misc.content-type-parser/transform-content-to body content-type)
                                                  :content-type content-type)
          {:keys [body] :as _request-result} (request-fn url-str request-params)
          coerced-data (misc.content-type-parser/json->edn body result-schema)
          _ (when (:error coerced-data)
              (clojure.pprint/pprint coerced-data))]
      coerced-data)))

(defn new-http [request-routes-key]
  (map->Http {:request-routes-key request-routes-key}))


(def response-mock (atom {}))

(defn with-response
  [endpoint method response]
  (swap! response-mock update-in [endpoint method] (fn [_] response)))
(defrecord HttpMock [config]
  component/Lifecycle

  (start [component]
    (assoc component :with-response with-response))

  (stop [component]
    component)
  IHttp
  (request [_this {:keys [method url]}]
    (get-in @response-mock [url method])))

(defn new-http-mock []
  (map->HttpMock {}))
