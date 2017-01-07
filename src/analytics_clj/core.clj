(ns analytics-clj.core
  (:import (com.segment.analytics Analytics)))

(defn initialize
  "Start building an Analytics instance."
  [write-key]
  (.build (Analytics/builder write-key)))
