(ns circleci.analytics-clj.core-test
  (:require [circleci.analytics-clj.core :as a]
            [circleci.analytics-clj.external :as e]
            [clojure.test :refer :all])
  (:import (com.segment.analytics Log)
           (java.util UUID)))

(defonce analytics (a/initialize "foobarbaz"))

(deftest test-initialize
  (testing "initialize an analytics client"
    (is (not (nil? analytics))))

  (testing "initialize an analytics client with logging"
    (let [called (atom false)]
      (letfn [(logger []
                (reify Log
                  (print [this level format args])
                  (print [this level error format args])))]
        (with-redefs [e/log* (fn [ab l]
                               (is (instance? Log l))
                               (reset! called true))]
          (is (not (nil? (a/initialize "foobarbaz" {:log (logger)}))))
          (is @called))))))

(deftest test-identify
  (testing "identify a user"
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
                                (is (= "company/name" (-> traits keys first)))
                                (reset! called true))]
        (a/identify analytics "1234" {:company/name "Acme Inc."})
        (is @called))))

  (testing "identify an anonymous user"
    (a/identify analytics nil {} {:anonymous-id (UUID/randomUUID)})))

(deftest test-track
  (testing "track a simple event"
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
      (with-redefs [e/enable-integration* (fn [mb integration enable?]
                                            (is (= "Amplitude" integration))
                                            (is (= false enable?))
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
  (testing "a simple screen call"
    (a/screen analytics "1234" "Login Screen"))

  (testing "a screen call with custom properties"
    (let [called (atom false)]
      (with-redefs [e/properties* (fn [mb properties]
                                    (is (= "path" (-> properties keys first)))
                                    (is (= "/users/login" (-> properties vals first)))
                                    (reset! called true))]
        (a/screen analytics "1234" "Login Screen" {:path "/users/login"})
        (is @called)))))

(deftest test-group
  (let [called (atom false)]
    (with-redefs [e/traits* (fn [mb traits]
                              (is (= "name" (-> traits keys first)))
                              (reset! called true))]
      (a/group analytics "1234" "group-5678" {:name "Segment"})
      (is @called))))

(deftest test-alias
  (testing "a simple alias"
    (a/alias analytics "1234" "5678")))
