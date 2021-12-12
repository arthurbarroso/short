(ns short.users.integration-test
  (:require [test-helpers :as th]
            [short.users.contracts :as c]
            [malli.core :as ml]
            [clojure.instant :as instant]
            [clojure.test :refer [deftest testing is]]))

(def user-body {:email "test@email.com"
                :password "arthurtest123@"
                :password-confirmation "arthurtest1232"})

(deftest users-integration-creation-test
  (testing "Creates an user"
    (let [{:keys [status body]}
          (th/endpoint-test :post "/v1/users"
                            {:body user-body})
          parsed-res {:user/email (:user/email body)
                      :user/uuid (java.util.UUID/fromString
                                  (:user/uuid body))
                      :user/created_at (instant/read-instant-date
                                        (:user/created_at body))}]
      (is (= 201 status))
      (is (true? (ml/validate c/UserOut parsed-res))))))
