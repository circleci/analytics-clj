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

  (testing-void "identify a user with traits"
                (a/identify analytics "1234" {"email" "foo@bar.com"}))

  (testing "identify a user with keyword traits"
    (with-redefs [e/traits* (fn [mb traits]
                              (is (= "email" (-> traits keys first))))]
      (a/identify analytics "1234" {:email "foo@bar.com"})))

  (testing "identify a user with namespaced keyword traits"
    (with-redefs [e/traits* (fn [mb traits]
                              (is (= "email/address" (-> traits keys first))))]
      (a/identify analytics "1234" {:email/address "foo@bar.com"})))

  (testing-void "identify an anonymous user"
                (a/identify analytics nil {} {:anonymous-id (UUID/randomUUID)})))

(deftest test-track
  (testing-void "track a simple event"
                (a/track analytics "1234" "signup"))

  (testing "track an event with custom properties"
    (with-redefs [e/properties* (fn [mb properties]
                                  (is (= "company" (-> properties keys first))))]
      (a/track analytics "1234" "signup" {"company" "Acme Inc."})
      (a/track analytics "1234" "signup" {:company "Acme Inc."})))

  (testing "disable an integration"
    (with-redefs [e/enable-integration* (fn [mb k v]
                                          (is (= "Amplitude" k))
                                          (is (= false v)))]
      (a/track analytics "1234" "signup" {"company" "Acme Inc."} {:integrations {"Amplitude" false}})))

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
        (is @called)))))
