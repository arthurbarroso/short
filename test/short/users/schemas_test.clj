(ns short.users.schemas-test
  (:require [short.users.schemas :as s]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is]]))

(deftest users-schema-user-test
  (testing "Matches the expected internal input"
    (let [input {:user/email "test"
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
  (testing "Matches the expected the expected internal user structure"
    (let [input {:user/email "string"
                 :user/password "string"
                 :user/uuid (java.util.UUID/randomUUID)
                 :user/created_at (java.util.Date.)
                 :user/active true
                 :db/id 1}]
      (is (true? (ml/validate s/ExistingUser input)))))
  (testing "Fails for an unexpected internal internal user structure"
    (let [input {:user/email 1}]
      (is (false? (ml/validate s/ExistingUser input))))))
