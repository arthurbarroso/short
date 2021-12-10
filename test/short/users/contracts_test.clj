(ns short.users.contracts-test
  (:require [short.users.contracts :as c]
            [malli.core :as ml]
            [clojure.test :refer [deftest testing is]]))

(deftest users-contract-userdata-test
  (testing "Matches the expected external input"
    (let [input {:email "test" :password "hithere"
                 :password-confirmation "hithere"}]
      (is (true? (ml/validate c/UserData input)))))
  (testing "Fails for an unexpected external input"
    (let [input {:email "test" :password "hithere"}]
      (is (false? (ml/validate c/UserData input))))))

(deftest users-contract-userout-test
  (testing "Matches the expected external output"
    (let [output {:user/email "test"
                  :user/uuid (java.util.UUID/randomUUID)
                  :user/created_at (java.util.Date.)}]
      (is (true? (ml/validate c/UserOut output)))))
  (testing "Fails for an unexpected external output"
    (let [output {:user/email "test"}]
      (is (false? (ml/validate c/UserOut output))))))
