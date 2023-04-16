(defproject purchase-listinator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.stuartsierra/component "1.1.0"]
                 [io.pedestal/pedestal.service "0.5.10"  :exclusions [org.clojure/clojure commons-io]]
                 [io.pedestal/pedestal.route "0.5.10"]
                 [io.pedestal/pedestal.jetty "0.5.10"]
                 [org.slf4j/slf4j-simple "1.7.36"]
                 [com.stuartsierra/component.repl "1.0.0"]
                 [prismatic/schema "1.3.0"]
                 [com.novemberain/monger "3.6.0"]
                 [com.taoensso/carmine "3.1.0"]
                 [prismatic/schema "1.3.0"]
                 [com.novemberain/langohr "5.1.0"]
                 [org.clojure/data.json "2.4.0"]
                 [camel-snake-kebab "0.4.3"]
                 [org.clojure/core.async "1.5.648" :exclusions [org.clojure/tools.reader]]
                 [clj-time "0.15.2"]
                 [funcool/cats "2.4.2"]
                 [nubank/matcher-combinators "3.5.0"]
                 [lein-cloverage "1.2.4"]
                 [nubank/state-flow "5.14.4"]
                 [clj-http "3.12.3"]
                 [clj-http-fake "1.0.3"]
                 [io.replikativ/datahike "0.6.1531" :exclusions [com.cognitect/transit-clj com.cognitect/transit-java org.clojure/clojurescript com.google.guava/guava com.taoensso/encore fress org.lz4/lz4-java com.taoensso/timbre]]]
  :test-paths ["test/unit" "test/integration"]
  :main purchase-listinator.core
  :aot [purchase-listinator.core]
  :profiles {:uberjar {:aot [purchase-listinator.core]}}
  :uberjar-name "purchase-listinator.jar"
  :repl-options {:init-ns purchase-listinator.core})
