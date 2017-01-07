(ns analytics-clj.core
  (:import (com.segment.analytics Analytics)
           (com.segment.analytics.internal AnalyticsClient)))

(defn initialize
  "Start building an Analytics instance."
  [write-key]
  (.build (Analytics/builder write-key)))

(defn flush
  "Flush events in the message queue."
  [^AnalyticsClient client]
  (.flush client))

(defn shutdown
  "Stops this instance from processing further requests."
  [^AnalyticsClient client]
  (.shutdown client))
