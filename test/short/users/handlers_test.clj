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
