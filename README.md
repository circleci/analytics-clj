# analytics-clj

Idiomatic Clojure wrapper for the Segment.io 2.x Java client

For full documentation on the Segment.io 2.x Java client, see [analytics-java](https://github.com/segmentio/analytics-java).

## Build Status

[![CircleCI](https://circleci.com/gh/circleci/analytics-clj/tree/master.svg?style=svg)](https://circleci.com/gh/circleci/analytics-clj/tree/master)

## Installation

*Not yet released*

## Usage

View the full [API](https://circleci.github.io/analytics-clj/).

#### Initialize an analytics client

`(def analytics (initialize "<writeKey>"))`

#### Identify a user

`(identify analytics "user-id")`

With traits:

`(identify analytics "user-id" {"email" "bob@acme.com"})`

#### Track an action of a user

`(track analytics "user-id" "signup")`

With properties:

`(track analytics "user-id" "signup" {"company" "Acme Inc."})`

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

## License

Copyright © 2017 CircleCI

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
