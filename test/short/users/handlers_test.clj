(ns short.users.handlers-test
  (:require [short.users.handlers :as h]
            [test-helpers :as th]
            [short.users.contracts :as c]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is use-fixtures]]))

(defonce database-conn (atom nil))

(use-fixtures :once
  (fn [f]
    (reset! database-conn (th/create-in-memory-db "users-tests-handlers"))
    (f)))

(def u
  {:email "arthur.test@test1.com"
   :password "test-password"
   :password-confirmation "test-password"})

(deftest users-create-user-handler-test
  (testing "Creates a new user from external data and prepares
            it to be sent in the output"
    (let [database database-conn
          result (h/create-user! u @database)]
      (is (true? (ml/validate c/UserOut result))))))

(deftest users-create-token-handler-test
  (testing "Creates a jwt token with the data supplied"
    (let [result (h/create-token!
                  {:auth {:jwt-secret "test"}}
                  {:user/email "arthur.test"})]
      (is (map? result))
      (is (string? (:token result))))))

(deftest users-hash-password-handler-test
  (testing "Creates a hash of a given password input"
    (let [result (h/hash-password! "arthurbarroso")]
      (is (string? result))
      (is (not (= "arthurbarrso" result))))))

(deftest users-compare-passwords-handler-test
  (testing "Returns true for a password input that matches
            the hashed password"
    (let [result (h/compare-passwords!
                  "chiclete"
                  (h/hash-password! "chiclete"))]
      (is (true? result))))
  (testing "Returns false for a password input that doesn't match
            the hashed password"
    (let [result (h/compare-passwords!
                  "chiclete"
                  (h/hash-password!
                   "chiclete1"))]
      (is (false? result)))))
