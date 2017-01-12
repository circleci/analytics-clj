(ns analytics-clj.utils
  (require [clojure.walk :as walk]))

(defn string-keys
  "Transform all map keys from keywords to strings while preserving namespacing (unlike clojure.walk/stringify-keys)."
  [m]
  (let [f (fn [[k v]]
            (if (keyword? k)
              [(str (.-sym k)) v]
              [(name k) v]))]
    (walk/postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) m)))
