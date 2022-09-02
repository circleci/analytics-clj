(ns circleci.analytics-clj.common
  (:require [circleci.analytics-clj.external :refer :all]
            [circleci.analytics-clj.utils :refer [string-keys]]))

(def ^:private ctx {"library" {"name" "analytics-clj"
                               "version" "0.8.1"}})

(defn common-fields
  "The `MessageBuilder` interface has a set of fields common to all messages.

  https://segment.com/docs/spec/common/"

  {:added "0.4.0"}

  [message-builder {:keys [anonymous-id context integration-options integrations timestamp message-id user-id]}]
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

      (cond-> (not (nil? message-id))
        (message-id* message-id))

      (cond-> (not (nil? user-id))
        (user-id* user-id)))))
