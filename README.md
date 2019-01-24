# analytics-clj

Idiomatic Clojure wrapper for the Segment.io 2.x Java client

For full documentation on the Segment.io 2.x Java client, see [analytics-java](https://github.com/segmentio/analytics-java).

## Build Status

[![CircleCI](https://circleci.com/gh/circleci/analytics-clj/tree/master.svg?style=svg)](https://circleci.com/gh/circleci/analytics-clj/tree/master)

## Installation

`[circleci/analytics-clj "0.4.1"]`

## Usage

View the full [API](https://circleci.github.io/analytics-clj/).

### Initialize an analytics client

```
(use '[circleci.analytics-clj.core])
(def analytics (initialize "<writeKey>"))
```

With logging:

```
(defn logger []
  (reify com.segment.analytics.Log
    (print [this level format args]
      (println (str (java.util.Date.) "\t" level "\t" args)))
    (print [this level error format args]
      (println error))))

(def analytics (initialize "<writeKey>" {:log (logger)}))
```

### Messages

All of the message types can take `options`. See [Spec: Common Fields](https://segment.com/docs/spec/common/) for a list of common fields between all message types.

#### Identify

`(identify analytics "user-id")`

With traits:

`(identify analytics "user-id" {:email "bob@acme.com"})`

#### Track

`(track analytics "user-id" "signup")`

With properties:

`(track analytics "user-id" "signup" {:company "Acme Inc."})`

A full example:

```
(track analytics (:id user) "signup" {:company "Acme Inc."} {:context {:language "en-us"}
                                                             :integrations {"AdRoll" false}
                                                             :integration-options {"Amplitude" {:session-id (:id session)}}})
```

#### Screen

`(screen analytics "1234" "Login Page")`

With properties:

`(screen analytics "1234" "Login Page" {:path "/users/login"})`

#### Group

`(group analytics "1234" "group-5678")`

With traits:

`(group analytics "1234" "group-5678" {:name "Segment"})`

#### Alias

`(alias analytics "anonymous_user" "5678")`

### Did we miss something?

We provided a top level `enqueue` function to allow you to do the following:

```
(enqueue analytics (doto (YourMessageType/builder)
                     (.userId "user-id")
                     (.properties {"company" "Acme Inc."})))
```

## License

Copyright © 2017 CircleCI

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
