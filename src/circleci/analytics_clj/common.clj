(ns circleci.analytics-clj.common
  (:require [circleci.analytics-clj.external :as external]
            [circleci.analytics-clj.utils :refer [string-keys]]))

(def ^:private ctx {"library" {"name" "analytics-clj"
                               "version" "0.8.2"}})

(defn common-fields
  "The `MessageBuilder` interface has a set of fields common to all messages.

  https://segment.com/docs/spec/common/"

  {:added "0.4.0"}

  [message-builder {:keys [anonymous-id context integration-options integrations timestamp message-id user-id]}]
  (letfn [(enable-integrations [message-builder integrations]
            (doseq [[integration enable?] integrations]
              (external/enable-integration* message-builder integration enable?)))

          (enable-integration-options [message-builder integration-options]
            (doseq [[integration options] integration-options]
              (external/integration-options* message-builder integration (string-keys options))))]

    (doto message-builder
      (external/context* (merge ctx (string-keys context)))

      (cond-> (not (nil? anonymous-id))
        (external/anonymous-id* anonymous-id))

      (cond-> (not (nil? integration-options))
        (enable-integration-options integration-options))

      (cond-> (not (nil? integrations))
        (enable-integrations integrations))

      (cond-> (not (nil? timestamp))
        (external/timestamp* timestamp))

      (cond-> (not (nil? message-id))
        (external/message-id* message-id))

      (cond-> (not (nil? user-id))
        (external/user-id* user-id)))))
