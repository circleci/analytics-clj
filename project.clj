(defproject circleci/analytics-clj "0.4.0"
  :description "Idiomatic Clojure wrapper for the Segment.io 2.x Java client"
  :url "https://github.com/circleci/analytics-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.segment.analytics.java/analytics "2.0.0-RC7"]]
  :plugins [[lein-codox "0.10.2"]]
  :codox {:output-path "docs"
          :namespaces [circleci.analytics-clj.core]})
