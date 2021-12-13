(ns short.users.controllers-test
  (:require [short.users.controllers :as cont]
            [test-helpers :as th]
            [clojure.test :refer [deftest testing is use-fixtures]]))

(defonce database-conn (atom nil))

(use-fixtures :once
  (fn [f]
    (reset! database-conn (th/create-in-memory-db "users-tests-controllers"))
    (f)))

(def u
  {:email "arthur.test@test1.com"
   :password "test-password"
   :password-confirmation "test-password"})

(deftest users-create-user-controller-test
  (testing "Creates a new user"
    (let [database database-conn
          {:keys [status body]}
          ((cont/create-user-controller! @database)
           {:parameters {:body u}})]
      (is (= 201 status))
      (is (= (:email u) (:user/email body))))))

(deftest users-login-controller-test
  (testing "Logs in with correct credentials"
    (let [database database-conn
          _ ((cont/create-user-controller! @database)
             {:parameters {:body {:email "arthurlogintest@test1.com"
                                  :password "test123"
                                  :password-confirmation "test123"}}})
          {:keys [status body]}
          ((cont/login-controller! {:database @database
                                    :auth {:jwt-secret "chiclete"}})
           {:parameters {:body {:email "arthurlogintest@test1.com"
                                :password "test123"}}})]
      (is (= 200 status))
      (is (string? (:token body)))))
  (testing "Fails for incorrect credentials"
    (let [database database-conn
          _ ((cont/create-user-controller! @database)
             {:parameters {:body {:email "arthurlogintest2@test1.com"
                                  :password "test123"
                                  :password-confirmation "test123"}}})
          {:keys [status]}
          ((cont/login-controller! {:database @database
                                    :auth {:jwt-secret "chiclete"}})
           {:parameters {:body {:email "arthurlogintest2@test1.com"
                                :password "test12345"}}})]
      (is (= 400 status))))
  (testing "Fails for a non existent user"
    (let [database database-conn
          {:keys [status]}
          ((cont/login-controller! {:database @database
                                    :auth {:jwt-secret "chiclete"}})
           {:parameters {:body {:email "arthurlogintest23413@test1.com"
                                :password "test12345"}}})]
      (is (= 400 status)))))
