(ns short.users.schemas-test
  (:require [short.users.schemas :as s]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is]]))

(deftest users-schema-user-test
  (testing "Matches the expected internal input"
    (let [input {:user/email "test@test.com"
                 :user/password "hithere"
                 :user/uuid (java.util.UUID/randomUUID)
                 :user/created_at (java.util.Date.)
                 :user/active true}]
      (is (true? (ml/validate s/User input)))))
  (testing "Fails for an unexpected internal input"
    (let [input {:email "test" :password "hithere"}]
      (is (false? (ml/validate s/User input))))))

(deftest users-schema-usertx-test
  (testing "Matches the expected internal tx input"
    (let [input {:db-before {}
                 :db-after {}
                 :tx-data {}
                 :tempids {1 2}}]
      (is (true? (ml/validate s/UserTx input)))))
  (testing "Fails for an unexpected internal tx input"
    (let [input {:db-before []}]
      (is (false? (ml/validate s/UserTx input))))))

(deftest users-schema-existinguser-test
  (testing "Matches the expected internal user structure"
    (let [input {:user/email "string@email.com"
                 :user/password "string"
                 :user/uuid (java.util.UUID/randomUUID)
                 :user/created_at (java.util.Date.)
                 :user/active true
                 :db/id 1}]
      (is (true? (ml/validate s/ExistingUser input)))))
  (testing "Fails for an unexpected internal user structure"
    (let [input {:user/email 1}]
      (is (false? (ml/validate s/ExistingUser input))))))

(deftest users-schema-tokenout-test
  (testing "Matches the expected internal token structure"
    (let [input {:token "something"}]
      (is (true? (ml/validate s/TokenOut input)))))
  (testing "Matches the expected internal token (empty) structure"
    (is (true? (ml/validate s/TokenOut nil))))
  (testing "Fails for an unexpected internal token structure"
    (let [input {:token2 "something"}]
      (is (false? (ml/validate s/TokenOut input))))))

(deftest users-schema-userqueryresult-test
  (testing "Matches the expected internal user query result structure"
    (let [user {:user/email "string@email.com"
                :user/password "string"
                :user/uuid (java.util.UUID/randomUUID)
                :user/created_at (java.util.Date.)
                :user/active true
                :db/id 1}
          input [[user]]]
      (is (true? (ml/validate s/UserQueryResult input)))))
  (testing "Matches the expected internal user query result (empty) structure"
    (is (true? (ml/validate s/UserQueryResult []))))
  (testing "Fails for an unexpected internal user query result structure"
    (let [input {:token2 "something"}]
      (is (false? (ml/validate s/UserQueryResult input))))))

(deftest users-schema-credentialscheck-test
  (testing "Matches the expected internal credentials check structure"
    (let [user {:user/email "string@email.com"
                :user/password "string"
                :user/uuid (java.util.UUID/randomUUID)
                :user/created_at (java.util.Date.)
                :user/active true
                :db/id 1}
          input {:matches? false
                 :existing-user user}]
      (is (true? (ml/validate s/CredentialsCheck input)))))
  (testing "Matches the expected internal credentials check (empty) structure"
    (is (true? (ml/validate s/CredentialsCheck {:matches? true :existing-user nil}))))
  (testing "Fails for an unexpected internal credentials check structure"
    (let [input {:token2 "something"}]
      (is (false? (ml/validate s/CredentialsCheck input))))))
