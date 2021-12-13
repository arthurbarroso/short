(ns short.users.handlers-test
  (:require [short.users.handlers :as h]
            [test-helpers :as th]
            [short.users.contracts :as c]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is use-fixtures]]
            [short.users.schemas :as s]))

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

(deftest users-check-credentials-handler-test
  (testing "Checks a valid user's credentials"
    (let [database database-conn
          _ (h/create-user!
             {:email "test-user-creds@test.com"
              :password "1234a"
              :password-confirmation "1234a"}
             @database)
          result (h/check-credentials! {:email "test-user-creds@test.com"
                                        :password "1234a"} @database)]
      (is (true? (:matches? result)))
      (is (= "test-user-creds@test.com" (:user/email (:existing-user result))))
      (is (true? (ml/validate s/CredentialsCheck result)))))
  (testing "Fails for invalid user credentials"
    (let [database database-conn
          _ (h/create-user!
             {:email "test-use123r@test.com"
              :password "1234a"
              :password-confirmation "1234a"}
             @database)
          result (h/check-credentials! {:email "test-use123r@test.com"
                                        :password "1234a3"} @database)]
      (is (false? (:matches? result)))
      (is (= "test-use123r@test.com" (:user/email (:existing-user result))))
      (is (true? (ml/validate s/CredentialsCheck result)))))
  (testing "Fails for a non existing user"
    (let [database database-conn
          result (h/check-credentials! {:email "test-user1@test.com"
                                        :password "1234a"} @database)]
      (is (false? (:matches? result)))
      (is (= nil (:user/email (:existing-user result))))
      (is (true? (ml/validate s/CredentialsCheck result))))))

(deftest users-gen-token-handler-test
  (testing "Generates a token for valid user credentials"
    (let [database database-conn
          _ (h/create-user!
             {:email "test-user-token@test.com"
              :password "1234a"
              :password-confirmation "1234a"}
             @database)
          creds (h/check-credentials! {:email "test-user-token@test.com"
                                       :password "1234a"} @database)
          result (h/gen-token! creds {:auth {:jwt-secret "xis"}})]
      (is (true? (ml/validate s/TokenOut result)))
      (is (string? (:token result)))))
  (testing "Fails for a non existing user"
    (let [database database-conn
          creds (h/check-credentials! {:email "test-user-non-existant-token@test.com"
                                       :password "1234a"} @database)
          result (h/gen-token! creds {:auth {:jwt-secret "xis"}})]
      (is (true? (ml/validate s/TokenOut result)))
      (is (nil? (:token result))))))
