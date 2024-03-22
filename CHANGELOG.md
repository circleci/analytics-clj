Version 0.10.1 (March 22, 2024)
================================

* [#25](https://github.com/circleci/analytics-clj/pull/25): Update CODEOWNERS with more relevant team
* [#24](https://github.com/circleci/analytics-clj/pull/24): Configure renovate
* [#26](https://github.com/circleci/analytics-clj/pull/26): Bump `lein codox`
* [#29](https://github.com/circleci/analytics-clj/pull/29): Add `docs` job to ensure `lein codox` continues to work
* [#32](https://github.com/circleci/analytics-clj/pull/32): Remove `lein ancient` plugin & its associated scheduled workflow now that we use Renovate
* [#31](https://github.com/circleci/analytics-clj/pull/31): Bump Clojure dependency to 1.11.2
* [#27](https://github.com/circleci/analytics-clj/pull/27): Bump transitive dependency okio-java and move it to managed-dependencies
* [#34](https://github.com/circleci/analytics-clj/pull/34): Bump `com.segment.analytics.java` dependency

Version 0.10.0 (March 22, 2024)
================================

Not pushed to Clojars because of a bug in the new automatic publishing CI job.


Version 0.8.2 (October 3, 2023)
================================

* [FIXED](https://github.com/circleci/analytics-clj/pull/21): Update com.segment.analytics.java/analytics to version 3.5.0
* [FIXED](https://github.com/circleci/analytics-clj/pull/21): Force update okio-jvm to version 3.4.0 fix CVE-2023-3635

Version 0.8.1 (September 1, 2022)
================================

* [FIXED](https://github.com/circleci/analytics-clj/pull/19): Update com.segment.analytics.java/analytics to version 3.3.1

Version 0.8.0 (January 27, 2019)
================================

* [REMOVED](https://github.com/circleci/analytics-clj/pull/10): Privatize circleci.analytics-clj.core/common-fields

Version 0.7.1 (January 27, 2019)
================================

* Support for Segment 2.1.1

Version 0.7.0 (January 27, 2019)
================================

* [ADDED](https://github.com/circleci/analytics-clj/pull/8): Allow sending a custom user-agent for HTTP requests.
* Support for Segment 2.1.0

Version 0.6.0 (January 27, 2019)
================================

* [ADDED](https://github.com/circleci/analytics-clj/pull/4): Allow sending a custom message ID.
* Support for Segment 2.0.0

Version 0.5.0 (January 24, 2019)
================================

* [ADDED](https://github.com/circleci/analytics-clj/pull/7): Add support for the page method https://segment.com/docs/sources/server/java/#page

Version 0.4.2 (January 24, 2019)
================================

* [FIXED](https://github.com/circleci/analytics-clj/pull/6): Fix the library version sent to Segment

Version 0.4.1 (April 6, 2017)
=============================

* [FIXED](https://github.com/circleci/analytics-clj/pull/2): Add support for Clojure 1.9

Version 0.4.0 (January 17, 2017)
================================

* Initial Release
* Support for Segment 2.x
