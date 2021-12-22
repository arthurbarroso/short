(ns short.users.logic-test
  (:require [short.users.logic :as l]
            [short.users.schemas :as s]
            [short.users.contracts :as c]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is]]
            [short.shared :as shared]))

(deftest users-internal->external-test
  (testing "Dissociates the internal-only fields of an user"
    (let [uuid (shared/generate-uuid!)
          now (shared/get-current-inst!)
          base-user {:user/email "oi@test.com"
                     :user/password "123super-secret"
                     :user/created_at now
                     :db/id 123
                     :user/active true
                     :user/uuid uuid}
          result (l/internal->external base-user)]
      (is (= {:user/email "oi@test.com"
              :user/uuid uuid
              :user/created_at now}
             result))
      (is (true? (ml/validate
                  c/UserOut
                  result))))))

(deftest users-user-creation-test
  (testing "Builds a map that conforms the s/User schema"
    (let [base-user {:email "oi@test.com" :password "hi"
                     :password-confirmation "hi"}
          uuid (shared/generate-uuid!)
          now (shared/get-current-inst!)
          result (l/user-creation base-user uuid now
                                  (:password base-user))]
      (is (= result
             {:user/email (:email base-user)
              :user/password (:password base-user)
              :user/uuid uuid
              :user/active true
              :user/created_at now}))
      (is (true? (ml/validate
                  s/User
                  result))))))
