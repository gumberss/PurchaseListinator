(ns utils.integration-test
  (:require [clojure.test :refer :all]
            [schema.core :as s]
            [state-flow.core :as state-flow]
            [purchase-listinator.core :as core]
            [com.stuartsierra.component :as component]
            [state-flow.cljtest :as cljtest]
            [purchase-listinator.purchase-listinator-core :as purchase-listinator-core]
            [purchase-listinator.modules.events.core :as modules.events.core]
            [purchase-listinator.modules.price-suggestion.core :as modules.price-suggestion.core]
            [purchase-listinator.modules.shopping-cart.core :as modules.shopping-cart.core]))

(def module-config-test
  [purchase-listinator-core/module-config-test
   modules.events.core/system-config-test
   modules.price-suggestion.core/system-config-test
   modules.shopping-cart.core/system-config-test
   ])

(defn system-config-test
  []
  (reduce conj
          {:env        "test"
           :web-server {:port (or (System/getenv "WEBSERVER_PORT") 3000)
                        :host (or (System/getenv "WEBSERVER_URL") "0.0.0.0")}}
          (map #(:system-config (%)) module-config-test)))

(defn new-system-test
  [config]
  (let [system-map (core/build-system-map (partial component/system-map :config config)
                                          (concat (core/general-components config)
                                                  (mapcat #(:system-components (%)) module-config-test)))]
    (system-map)))

(defn get-component
  [system & component-path]
  (get-in system (flatten component-path)))

(defn start-system
  []
  (-> (new-system-test (system-config-test))
      component/start))

(defn stop-system
  [system]
  (component/stop system))

(defn custom-runner
  [flow state]
  (s/with-fn-validation (state-flow/run flow state)))

(defmacro integration-test
  [name & flows]
  `(cljtest/defflow ~name {:init    start-system
                           :cleanup stop-system
                           :runner  custom-runner}
                    ~@flows))
