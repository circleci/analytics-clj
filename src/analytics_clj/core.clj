(ns analytics-clj.core
  (:import (com.segment.analytics Analytics)
           (com.segment.analytics.internal AnalyticsClient)
           (com.segment.analytics.messages IdentifyMessage ImmutableMap)))

(defn initialize
  "Start building an Analytics instance."
  [write-key]
  (.build (Analytics/builder write-key)))

(defn- full-name
  "Returns the full name of the map key. If it's a symbol, retrieves the full namespaced name and returns that instead."
  [k]
  (if (keyword? k)
    (str (.-sym k))
    (name k)))

(defn map->immutable-map [m]
  (reduce (fn [builder [k v]]
            (.put builder (full-name k) v))
          (ImmutableMap/builder)
          m))

(defn identify
  "`identify` lets you tie a user to their actions and
  record traits about them. It includes a unique User ID
  and any optional traits you know about them."
  ([^AnalyticsClient client ^String user-id]
   (identify client user-id {}))
  ([^AnalyticsClient client ^String user-id traits]
   (let [traits (.build (map->immutable-map traits))]
     (.enqueue client (doto (IdentifyMessage/builder)
                        (.userId user-id)
                        (.traits traits))))))

(defn track
  "`track` lets you record the actions your users perform.
  Every action triggers what we call an “event”, which can
  also have associated properties."
  ([^AnalyticsClient client ^String user-id ^String event]
   (track client user-id event {}))
  ([^AnalyticsClient client ^String user-id ^String event properties]
   ;; TODO
   ))

(defn screen
  "The `screen` method lets you you record whenever a user
  sees a screen of your mobile app, along with optional
  extra information about the page being viewed."
  ([^AnalyticsClient client ^String user-id ^String name ^String category]
   (screen client user-id name category {}))
  ([^AnalyticsClient client ^String user-id ^String name ^String category properties]
   ;; TODO
   ))

(defn group
  "`group` lets you associate an identified user user with
  a group. A group could be a company, organization, account,
  project or team! It also lets you record custom traits
  about the group, like industry or number of employees."
  ([^AnalyticsClient client ^String user-id ^String group-id]
   (group client user-id group-id {}))
  ([^AnalyticsClient client ^String user-id ^String group-id traits]
   ;; TODO
   ))

(defn alias
  "`alias` is how you associate one identity with another.
  This is an advanced method, but it is required to manage
  user identities successfully in some of our integrations."
  [^AnalyticsClient client ^String previous-id ^String user-id]
  ;; TODO
  )

(defn flush
  "Flush events in the message queue."
  [^AnalyticsClient client]
  (.flush client))

(defn shutdown
  "Stops this instance from processing further requests."
  [^AnalyticsClient client]
  (.shutdown client))
