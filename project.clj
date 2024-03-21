(defproject circleci/analytics-clj "0.8.2"
  :description "Idiomatic Clojure wrapper for the Segment.io 2.x Java client"
  :url "https://github.com/circleci/analytics-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.2"]
                 [com.segment.analytics.java/analytics "3.5.1"]]
  :managed-dependencies [[com.squareup.okio/okio-jvm "3.9.0"]]
  :profiles {:dev {:dependencies [[bond "0.2.6"]]}}
  :plugins [[lein-codox "0.10.8"]]
  :codox {:output-path "docs"
          :namespaces [circleci.analytics-clj.core]})
