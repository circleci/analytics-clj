(ns circleci.analytics-clj.core-test
  (:require [bond.james :as bond]
            [circleci.analytics-clj.core :as a]
            [circleci.analytics-clj.external :as e]
            [clojure.test :refer :all])
  (:import (com.segment.analytics Log)
           (java.util UUID)))

(defonce analytics (a/initialize "foobarbaz"))

(deftest test-initialize
  (testing "initialize an analytics client"
    (is (not (nil? analytics))))

  (testing "initialize an analytics client with logging"
    (letfn [(logger []
              (reify Log
                (print [this level format args])
                (print [this level error format args])))]
      (bond/with-spy [e/log*]
        (is (not (nil? (a/initialize "foobarbaz" {:log (logger)}))))
        (is (= 1 (-> e/log* bond/calls count)))
        (is (instance? Log (-> e/log* bond/calls first :args second)))))))

(deftest test-identify
  (testing "identify a user"
    (a/identify analytics "1234"))

  (testing "identify a user with traits"
    (bond/with-spy [e/traits*]
      (a/identify analytics "1234" {"email" "foo@bar.com"})
      (is (= 1 (-> e/traits* bond/calls count)))
      (is (= "email" (-> e/traits* bond/calls first :args second keys first)))

      (a/identify analytics "1234" {:email "foo@bar.com"})
      (is (= 2 (-> e/traits* bond/calls count)))
      (is (= "email" (-> e/traits* bond/calls first :args second keys first)))))

  (testing "identify a user with namespaced keyword traits"
    (bond/with-spy [e/traits*]
      (a/identify analytics "1234" {:company/name "Acme Inc."})
      (is (= 1 (-> e/traits* bond/calls count)))
      (is (= "company/name" (-> e/traits* bond/calls first :args second keys first)))))

  (testing "identify an anonymous user"
    (a/identify analytics nil {} {:anonymous-id (UUID/randomUUID)})))

(deftest test-track
  (testing "track a simple event"
    (a/track analytics "1234" "signup"))

  (testing "track an event with custom properties"
    (bond/with-spy [e/properties*]
      (a/track analytics "1234" "signup" {"company" "Acme Inc."})
      (is (= 1 (-> e/properties* bond/calls count)))
      (is (= "company" (-> e/properties* bond/calls first :args second keys first)))

      (a/track analytics "1234" "signup" {:company "Acme Inc."})
      (is (= 2 (-> e/properties* bond/calls count)))
      (is (= "company" (-> e/properties* bond/calls first :args second keys first)))))

  (testing "disable an integration"
    (bond/with-spy [e/enable-integration*]
      (a/track analytics "1234" "signup" {"company" "Acme Inc."} {:integrations {"Amplitude" false}})
      (is (= 1 (-> e/enable-integration* bond/calls count)))
      (is (= "Amplitude" (-> e/enable-integration* bond/calls first :args second)))
      (is (= false (-> e/enable-integration* bond/calls first :args (nth 2))))))

  (testing "custom context is merged with library context"
    (bond/with-spy [e/context*]
      (a/track analytics "1234" "signup" {"company" "Acme Inc."} {:context {:language "en-us"}})
      (is (= 1 (-> e/context* bond/calls count)))
      (is (= #{"library" "language"} (-> e/context* bond/calls first :args second keys set)))))

  (testing "integration options"
    (bond/with-spy [e/integration-options*]
      (a/track analytics "1234" "signup" {"company" "Acme Inc."} {:integration-options {"Amplitude" {:session-id "1234567890"}}})
      (is (= 1 (-> e/integration-options* bond/calls count)))
      (is (= "Amplitude" (-> e/integration-options* bond/calls first :args second)))
      (is (= "session-id" (-> e/integration-options* bond/calls first :args (nth 2) keys first)))
      (is (= "1234567890" (-> e/integration-options* bond/calls first :args (nth 2) vals first)))))

  (testing "sending a custom timestamp"
    (let [timestamp (java.util.Date.)]
      (bond/with-spy [e/timestamp*]
        (a/track analytics "1234" "signup" {"company" "Acme Inc."} {:timestamp timestamp})
        (is (= 1 (-> e/timestamp* bond/calls count)))
        (is (= timestamp (-> e/timestamp* bond/calls first :args second)))))))

(deftest test-screen
  (testing "a simple screen call"
    (a/screen analytics "1234" "Login Screen"))

  (testing "a screen call with custom properties"
    (bond/with-spy [e/properties*]
      (a/screen analytics "1234" "Login Screen" {:path "/users/login"})
      (is (= 1 (-> e/properties* bond/calls count)))
      (is (= "path" (-> e/properties* bond/calls first :args second keys first)))
      (is (= "/users/login" (-> e/properties* bond/calls first :args second vals first))))))

(deftest test-page
  (testing "a simple page call"
    (a/page analytics "1234" "Login Page"))

  (testing "a apge call with custom properties"
    (bond/with-spy [e/properties*]
      (a/page analytics "1234" "Login Page" {:path "/users/login"})
      (is (= 1 (-> e/properties* bond/calls count)))
      (is (= "path" (-> e/properties* bond/calls first :args second keys first)))
      (is (= "/users/login" (-> e/properties* bond/calls first :args second vals first))))))

(deftest test-group
  (bond/with-spy [e/traits*]
    (a/group analytics "1234" "group-5678" {:name "Segment"})
    (is (= 1 (-> e/traits* bond/calls count)))
    (is (= "name" (-> e/traits* bond/calls first :args second keys first)))))

(deftest test-alias
  (testing "a simple alias"
    (a/alias analytics "1234" "5678")))
