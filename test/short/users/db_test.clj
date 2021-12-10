(ns short.users.db-test
  (:require [short.users.db :as db]
            [short.database :refer [schema]]
            [short.users.schemas :as s]
            [malli.core :as m]
            [datahike.api :as d]
            [clojure.test :refer [deftest testing is use-fixtures]]))

(defonce database-conn (atom nil))

(defn create-in-memory-db [id]
  (let [config {:store {:backend :mem
                        :id id}}
        _ (d/create-database config)
        connection (d/connect config)]
    (d/transact connection schema)
    connection))

(use-fixtures :once
  (fn [f]
    (reset! database-conn (create-in-memory-db "users-tests"))
    (f)))

(def user
  {:user/email "arthur.test@test.com"
   :user/password "test-password"
   :user/uuid (java.util.UUID/randomUUID)
   :user/created_at (java.util.Date.)
   :user/active true})

(deftest users-create-user-test
  (testing "Creates a new user"
    (let [database database-conn
          result (db/create-user! user @database)]
      (is (not (nil? (:tx-data result))))
      (is (true? (m/validate s/UserTx result))))))
