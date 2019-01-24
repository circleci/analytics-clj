(ns circleci.analytics-clj.core
  (:refer-clojure :exclude [alias flush])
  (:require [circleci.analytics-clj.external :refer :all]
            [circleci.analytics-clj.utils :refer [string-keys]])
  (:import (com.segment.analytics Analytics)
           (com.segment.analytics.messages AliasMessage
                                           GroupMessage
                                           IdentifyMessage
                                           ScreenMessage
                                           TrackMessage)))

(def ^:private ctx {"library" {"name" "analytics-clj"
                               "version" "0.4.2"}})

(defn initialize
  "Start building an Analytics instance."

  {:added "0.4.0"}

  ([write-key]
   (initialize write-key nil))

  ([write-key {:keys [client log endpoint network-executor callback]}]
   (.build (doto (Analytics/builder write-key)
             (cond-> (not (nil? client))
               (client* client))

             (cond-> (not (nil? log))
               (log* log))

             (cond-> (not (nil? endpoint))
               (endpoint* endpoint))

             (cond-> (not (nil? network-executor))
               (network-executor* network-executor))

             (cond-> (not (nil? callback))
               (callback* callback))))))

(defn enqueue
  "Top-level `enqueue` function to allow for extensibility in the future."
  {:added "0.4.0"}
  [^Analytics analytics message-builder]
  (.enqueue analytics message-builder))

(defn flush
  "Flush events in the message queue."
  {:added "0.4.0"}
  [^Analytics analytics]
  (.flush analytics))

(defn shutdown
  "Stops this instance from processing further requests."
  {:added "0.4.0"}
  [^Analytics analytics]
  (.shutdown analytics))

(defn common-fields
  "The `MessageBuilder` interface has a set of fields common to all messages.

  https://segment.com/docs/spec/common/"

  {:added "0.4.0"}

  [message-builder {:keys [anonymous-id context integration-options integrations timestamp user-id]}]
  (letfn [(enable-integrations [message-builder integrations]
            (doseq [[integration enable?] integrations]
              (enable-integration* message-builder integration enable?)))

          (enable-integration-options [message-builder integration-options]
            (doseq [[integration options] integration-options]
              (integration-options* message-builder integration (string-keys options))))]

    (doto message-builder
      (context* (merge ctx (string-keys context)))

      (cond-> (not (nil? anonymous-id))
        (anonymous-id* anonymous-id))

      (cond-> (not (nil? integration-options))
        (enable-integration-options integration-options))

      (cond-> (not (nil? integrations))
        (enable-integrations integrations))

      (cond-> (not (nil? timestamp))
        (timestamp* timestamp))

      (cond-> (not (nil? user-id))
        (user-id* user-id)))))

(defn identify
  "`identify` lets you tie a user to their actions and
  record traits about them. It includes a unique User ID
  and any optional traits you know about them."

  {:added "0.4.0"}

  ([^Analytics analytics user-id]
   (identify analytics user-id nil nil))

  ([^Analytics analytics user-id traits]
   (identify analytics user-id traits nil))

  ([^Analytics analytics user-id traits options]
   (enqueue analytics (doto (IdentifyMessage/builder)
                        (common-fields (merge {:user-id user-id} options))
                        (cond-> (not (nil? traits)) (traits* (string-keys traits)))))))

(defn track
  "`track` lets you record the actions your users perform.
  Every action triggers what we call an “event”, which can
  also have associated properties."

  {:added "0.4.0"}

  ([^Analytics analytics user-id event]
   (track analytics user-id event nil nil))

  ([^Analytics analytics user-id event properties]
   (track analytics user-id event properties nil))

  ([^Analytics analytics user-id event properties options]
   (enqueue analytics (doto (TrackMessage/builder event)
                        (common-fields (merge {:user-id user-id} options))
                        (cond-> (not (nil? properties)) (properties* (string-keys properties)))))))

(defn screen
  "The `screen` method lets you you record whenever a user
  sees a screen of your mobile app, along with optional
  extra information about the page being viewed."

  {:added "0.4.0"}

  ([^Analytics analytics user-id name]
   (screen analytics user-id name nil nil))

  ([^Analytics analytics user-id name properties]
   (screen analytics user-id name properties nil))

  ([^Analytics analytics user-id name properties options]
   (enqueue analytics (doto (ScreenMessage/builder name)
                        (common-fields (merge {:user-id user-id} options))
                        (cond-> (not (nil? properties)) (properties* (string-keys properties)))))))

(defn group
  "`group` lets you associate an identified user with
  a group. A group could be a company, organization, account,
  project or team! It also lets you record custom traits
  about the group, like industry or number of employees."

  {:added "0.4.0"}

  ([^Analytics analytics user-id group-id]
   (group analytics user-id group-id nil nil))

  ([^Analytics analytics user-id group-id traits]
   (group analytics user-id group-id traits nil))

  ([^Analytics analytics user-id group-id traits options]
   (enqueue analytics (doto (GroupMessage/builder group-id)
                        (common-fields (merge {:user-id user-id} options))
                        (cond-> (not (nil? traits)) (traits* (string-keys traits)))))))

(defn alias
  "`alias` is how you associate one identity with another.
  This is an advanced method, but it is required to manage
  user identities successfully in some of our integrations."

  {:added "0.4.0"}

  ([^Analytics analytics previous-id user-id]
   (alias analytics previous-id user-id nil))

  ([^Analytics analytics previous-id user-id options]
   (enqueue analytics (doto (AliasMessage/builder previous-id)
                        (common-fields (merge {:user-id user-id} options))))))
