(ns analytics-clj.core
  (:import (com.segment.analytics Analytics)
           (com.segment.analytics.messages IdentifyMessage MessageBuilder)))

(def context {"library" "analytics-clj"})

(defn initialize
  "Start building an Analytics instance."
  [write-key]
  (.build (Analytics/builder write-key)))

(defn enqueue
  "Top-level `enqueue` function to allow for extensibility in the future."
  [^Analytics analytics ^MessageBuilder message]
  (.enqueue analytics message))

(defn identify
  "`identify` lets you tie a user to their actions and
  record traits about them. It includes a unique User ID
  and any optional traits you know about them."
  ([^Analytics analytics user-id]
   (identify analytics user-id {}))
  ([^Analytics analytics user-id traits]
   (identify analytics user-id traits {}))
  ([^Analytics analytics user-id traits {:keys [context anonymous-id]}]
   (enqueue analytics (doto (IdentifyMessage/builder)
                        (.userId user-id)
                        (cond-> (not (nil? context)) (.context context))
                        (cond-> (not (nil? anonymous-id)) (.anonymousId anonymous-id))
                        (cond-> (not (nil? traits)) (.traits traits))))))

(defn track
  "`track` lets you record the actions your users perform.
  Every action triggers what we call an “event”, which can
  also have associated properties."
  ([^Analytics analytics user-id event]
   (track analytics user-id event {}))
  ([^Analytics analytics user-id event properties]
   ;; TODO
   ))

(defn screen
  "The `screen` method lets you you record whenever a user
  sees a screen of your mobile app, along with optional
  extra information about the page being viewed."
  ([^Analytics analytics user-id name category]
   (screen analytics user-id name category {}))
  ([^Analytics analytics user-id name category properties]
   ;; TODO
   ))

(defn group
  "`group` lets you associate an identified user user with
  a group. A group could be a company, organization, account,
  project or team! It also lets you record custom traits
  about the group, like industry or number of employees."
  ([^Analytics analytics user-id group-id]
   (group analytics user-id group-id {}))
  ([^Analytics analytics user-id group-id traits]
   ;; TODO
   ))

(defn alias
  "`alias` is how you associate one identity with another.
  This is an advanced method, but it is required to manage
  user identities successfully in some of our integrations."
  [^Analytics analytics previous-id user-id]
  ;; TODO
  )

(defn flush
  "Flush events in the message queue."
  [^Analytics analytics]
  (.flush analytics))

(defn shutdown
  "Stops this instance from processing further requests."
  [^Analytics analytics]
  (.shutdown analytics))
