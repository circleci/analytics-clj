(ns analytics-clj.core-test
  (:require [clojure.test :refer :all]
            [analytics-clj.core :refer :all]))

(defonce client (initialize "foobarbaz"))

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
    (is (not (nil? client)))))

(deftest test-identify
  (testing-void "we're able to identify a user"
                (identify client "1234"))

  (testing-void "identify a user with traits"
                (identify client "1234" {"email" "foo@bar.com"})))
