(ns short.users.handlers
  (:require [short.users.logic :as l]
            [short.users.contracts :as c]
            [short.users.db :as db]
            [short.shared :as shared]
            [buddy.hashers :as bh]
            [buddy.sign.jwt :as jwt]))

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

(defn create-token!
  {:malli/schema [:=> [:cat
                       [:map [:auth [:map [:jwt-secret :string]]]]
                       c/UserOut]
                  [:map [:token :string]]]}
  [env payload]
  {:token (jwt/sign payload (get-in env [:auth :jwt-secret]))})

(defn hash-password! [password-input]
  {:malli/schema [:=> [:cat :string] :string]}
  (bh/encrypt password-input))

(defn compare-passwords! [password-input user-password]
  {:malli/schema [:=> [:cat :string :string] :boolean]}
  (bh/check password-input user-password))
