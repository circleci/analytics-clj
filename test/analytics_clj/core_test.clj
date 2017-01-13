(ns analytics-clj.core-test
  (:require [clojure.test :refer :all]
            [analytics-clj.core :as a]
            [analytics-clj.external :as e])
  (:import (java.util UUID)))

(defonce analytics (a/initialize "foobarbaz"))

(defmacro testing-void
  "When you want to test a Java function that returns void.
  Just to be sure it doesn't throw an exception."
  [description & body]
  `(testing ~description
     (try
       ~@body
       (finally
         (is true)))))

(deftest test-initialize
  (testing "initialize an analytics client"
    (is (not (nil? analytics)))))

(deftest test-identify
  (testing-void "we're able to identify a user"
                (a/identify analytics "1234"))

  (testing "identify a user with traits"
    (let [called (atom false)]
      (with-redefs [e/traits* (fn [mb traits]
                                (is (= "email" (-> traits keys first)))
                                (reset! called true))]
        (a/identify analytics "1234" {"email" "foo@bar.com"})
        (is @called)
        (reset! called false)
        (a/identify analytics "1234" {:email "foo@bar.com"})
        (is @called))))

  (testing "identify a user with namespaced keyword traits"
    (let [called (atom false)]
      (with-redefs [e/traits* (fn [mb traits]
                                (is (= "email/address" (-> traits keys first)))
                                (reset! called true))]
        (a/identify analytics "1234" {:email/address "foo@bar.com"})
        (is @called))))

  (testing-void "identify an anonymous user"
                (a/identify analytics nil {} {:anonymous-id (UUID/randomUUID)})))

(deftest test-track
  (testing-void "track a simple event"
                (a/track analytics "1234" "signup"))

  (testing "track an event with custom properties"
    (let [called (atom false)]
      (with-redefs [e/properties* (fn [mb properties]
                                    (is (= "company" (-> properties keys first)))
                                    (reset! called true))]
        (a/track analytics "1234" "signup" {"company" "Acme Inc."})
        (a/track analytics "1234" "signup" {:company "Acme Inc."})
        (is @called))))

  (testing "disable an integration"
    (let [called (atom false)]
      (with-redefs [e/enable-integration* (fn [mb k v]
                                            (is (= "Amplitude" k))
                                            (is (= false v))
                                            (reset! called true))]
        (a/track analytics "1234" "signup" {"company" "Acme Inc."} {:integrations {"Amplitude" false}})
        (is @called))))

  (testing "custom context is merged with library context"
    (let [called (atom false)]
      (with-redefs [e/context* (fn [mb c]
                                 (is (= #{"library" "language"} (set (keys c))))
                                 (reset! called true))]
        (a/track analytics "1234" "signup" {"company" "Acme Inc."} {:context {:language "en-us"}})
        (is @called))))

  (testing "integration options"
    (let [called (atom false)]
      (with-redefs [e/integration-options* (fn [mb i o]
                                             (is (= "Amplitude" i))
                                             (is (= "session-id" (-> o keys first)))
                                             (is (= "1234567890" (-> o vals first)))
                                             (reset! called true))]
        (a/track analytics "1234" "signup" {"company" "Acme Inc."} {:integration-options {"Amplitude" {:session-id "1234567890"}}})
        (is @called))))

  (testing "sending a custom timestamp"
    (let [called (atom false)
          timestamp (java.util.Date.)]
      (with-redefs [e/timestamp* (fn [mb t]
                                   (is (= t timestamp))
                                   (reset! called true))]
        (a/track analytics "1234" "signup" {"company" "Acme Inc."} {:timestamp timestamp})
        (is @called)))))

(deftest test-screen
  (testing-void "a simple screen call"
                (a/screen analytics "1234" "Login Page"))

  (testing "a screen call with custom properties"
    (let [called (atom false)]
      (with-redefs [e/properties* (fn [mb properties]
                                    (is (= "path" (-> properties keys first)))
                                    (is (= "/users/login" (-> properties vals first)))
                                    (reset! called true))]
        (a/screen analytics "1234" "Login Page" {:path "/users/login"})
        (is @called)))))

(deftest test-group
  (let [called (atom false)]
    (with-redefs [e/traits* (fn [mb traits]
                              (is (= "name" (-> traits keys first)))
                              (reset! called true))]
      (a/group analytics "1234" "group-5678" {:name "Segment"})
      (is @called))))
