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
