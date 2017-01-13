(ns analytics-clj.external
  (:import (com.segment.analytics.messages MessageBuilder)))

(defn enable-integration* [^MessageBuilder message-builder k v]
  (doto message-builder
    (.enableIntegration k v)))

(defn integration-options* [^MessageBuilder message-builder integration options]
  (doto message-builder
    (.integrationOptions integration options)))

(defn context* [^MessageBuilder message-builder context]
  (doto message-builder
    (.context context)))

(defn traits* [^MessageBuilder message-builder traits]
  (doto message-builder
    (.traits traits)))

(defn properties* [^MessageBuilder message-builder properties]
  (doto message-builder
    (.properties properties)))
