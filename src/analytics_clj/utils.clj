(ns analytics-clj.utils
  (:import (com.segment.analytics.messages ImmutableMap)))


(defn- full-name
  "Returns the full name of the map key. If it's a
  symbol, retrieves the full namespaced name and
  returns that instead."
  [k]
  (if (keyword? k)
    (str (.-sym k))
    (name k)))

(defn map->immutable-map [m]
  (.build
   (reduce (fn [builder [k v]]
             (.put builder (full-name k) v))
           (ImmutableMap/builder)
           m)))
