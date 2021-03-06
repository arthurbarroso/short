(ns short.users.db-test
  (:require [short.users.db :as db]
            [short.users.schemas :as s]
            [malli.core :as m]
            [test-helpers :as th]
            [clojure.test :refer [deftest testing is use-fixtures]]
            [short.shared :as shared]))

(defonce database-conn (atom nil))

(use-fixtures :once
  (fn [f]
    (reset! database-conn (th/create-in-memory-db "users-tests"))
    (f)))

(defn create-user [opts]
  (merge
   {:user/email "arthur.test@test.com"
    :user/password "test-password"
    :user/uuid (shared/generate-uuid!)
    :user/created_at (shared/get-current-inst!)
    :user/active true}
   opts))

(deftest users-create-user-test
  (testing "Creates a new user"
    (let [database database-conn
          result (db/create-user! (create-user {}) @database)]
      (is (not (nil? (:tx-data result))))
      (is (true? (m/validate shared/Transaction result))))))

(deftest users-get-user-test
  (testing "Finds an user"
    (let [database database-conn
          usertx (db/create-user! (create-user
                                   {:user/email "find@test.com"})
                                  @database)
          eid (shared/tempid->eid usertx)
          result (db/get-user! eid @database)]
      (is (true? (m/validate s/ExistingUser result))))))

(deftest users-get-user-test
  (testing "Finds an user by it's uuid"
    (let [u (create-user {:user/email "uuid@email.com"})
          database database-conn
          _ (db/create-user! u
                             @database)
          result (db/get-user-by-uuid! (:user/uuid u) @database)]
      (is (true? (m/validate s/UserQueryResult result)))))
  (testing "Returns nil for a non-existent uuid"
    (let [database database-conn
          result (db/get-user-by-uuid! (shared/generate-uuid!) @database)]
      (is (empty? result))
      (is (true? (m/validate s/UserQueryResult result))))))

(deftest users-get-user-by-email-test
  (testing "Finds an user by it's email"
    (let [u (create-user {:user/email "uuiddvvv@email.com"})
          database database-conn
          _ (db/create-user! u
                             @database)
          result (db/get-user-by-email! (:user/email u) @database)]
      (is (true? (m/validate s/UserQueryResult result)))))
  (testing "Returns nil for a non-existent e-mail"
    (let [u (create-user {:user/email "uuidd1@email.com"})
          database database-conn
          result (db/get-user-by-email! (:user/email u) @database)]
      (is (empty? result))
      (is (true? (m/validate s/UserQueryResult result))))))
