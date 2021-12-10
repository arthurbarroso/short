(ns short.users.handlers
  (:require [short.users.logic :as l]
            [short.users.contracts :as c]
            [short.users.db :as db]
            [short.shared :as shared]))

(defn create-user!
  {:malli/schema [:=> [:cat c/UserData :any] c/UserOut]}
  [new-user db]
  (let [id (shared/generate-uuid!)
        created_at (shared/get-current-inst!)]
    (-> new-user
        (l/user-creation id created_at)
        (db/create-user! db)
        l/tx->id
        (db/get-user! db)
        l/internal->external)))
