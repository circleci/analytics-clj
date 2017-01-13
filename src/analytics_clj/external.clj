(ns analytics-clj.external
  (:import (com.segment.analytics.messages MessageBuilder)))

(defn anonymous-id* [^MessageBuilder message-builder anonymous-id]
  (doto message-builder
    (.anonymousId anonymous-id)))

(defn context* [^MessageBuilder message-builder context]
  (doto message-builder
    (.context context)))

(defn enable-integration* [^MessageBuilder message-builder integration enable?]
  (doto message-builder
    (.enableIntegration integration enable?)))

(defn integration-options* [^MessageBuilder message-builder integration options]
  (doto message-builder
    (.integrationOptions integration options)))

(defn log* [analytics-builder log]
  (doto analytics-builder
    (.log log)))

(defn properties* [^MessageBuilder message-builder properties]
  (doto message-builder
    (.properties properties)))

(defn timestamp* [^MessageBuilder message-builder timestamp]
  (doto message-builder
    (.timestamp timestamp)))

(defn traits* [^MessageBuilder message-builder traits]
  (doto message-builder
    (.traits traits)))

(defn user-id* [^MessageBuilder message-builder user-id]
  (doto message-builder
    (.userId user-id)))
