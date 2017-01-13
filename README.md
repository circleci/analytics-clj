# analytics-clj

Idiomatic Clojure wrapper for the Segment.io 2.x Java client

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

TODO

#### Group

TODO

#### Alias

TODO

## License

Copyright © 2017 CircleCI

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
