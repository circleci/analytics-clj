(ns circleci.analytics-clj.external
  (:import (com.segment.analytics.messages MessageBuilder)))

;;; com.segment.analytics.Analytics$Builder

(defn client* [^com.segment.analytics.Analytics$Builder analytics-builder client]
  (doto analytics-builder
    (.client client)))

(defn log* [^com.segment.analytics.Analytics$Builder analytics-builder log]
  (doto analytics-builder
    (.log log)))

(defn endpoint* [^com.segment.analytics.Analytics$Builder analytics-builder endpoint]
  (doto analytics-builder
    (.endpoint endpoint)))

(defn network-executor* [^com.segment.analytics.Analytics$Builder analytics-builder network-executor]
  (doto analytics-builder
    (.networkExecutor network-executor)))

(defn callback* [^com.segment.analytics.Analytics$Builder analytics-builder callback]
  (doto analytics-builder
    (.callback callback)))

;;; com.segment.analytics.messages MessageBuilder

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

(defn timestamp* [^MessageBuilder message-builder timestamp]
  (doto message-builder
    (.timestamp timestamp)))

(defn message-id* [^MessageBuilder message-builder message-id]
  (doto message-builder
    (.messageId message-id)))

(defn user-id* [^MessageBuilder message-builder user-id]
  (doto message-builder
    (.userId user-id)))

;;; com.segment.analytics.messages.TrackMessage$Builder

(defn properties* [^com.segment.analytics.messages.TrackMessage$Builder message-builder properties]
  (doto message-builder
    (.properties properties)))

;;; com.segment.analytics.messages.IdentifyMessage$Builder

(defn traits* [^com.segment.analytics.messages.IdentifyMessage$Builder message-builder traits]
  (doto message-builder
    (.traits traits)))
