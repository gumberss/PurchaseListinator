(ns utils.integration-test
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [state-flow.core :as state-flow]
            [purchase-listinator.core :as core]
            [com.stuartsierra.component :as component]))

(def system-test-config
  {:env        :test
   :web-server {:port 5150
                :host "192.168.1.104"}
   :datomic    {:db-uri (str "datomic:mem://" (random-uuid))}})

(defn get-component
  [system & component-path]
  (get-in system (flatten component-path)))

(defn start-system
  []
  (-> (core/new-system-test system-test-config)
      component/start))

(defn custom-runner
  [flow state]
  (s/with-fn-validation (state-flow/run flow state)))

(defmacro integration-test
  [name & flows]
  `(state-flow.cljtest/defflow ~name {:init   start-system
                                      :runner custom-runner}
                               ~@flows))
