(ns short.users.handlers
  (:require [short.users.logic :as l]
            [short.users.contracts :as c]
            [short.users.db :as db]
            [short.shared :as shared]
            [buddy.hashers :as bh]
            [buddy.sign.jwt :as jwt]
            [short.users.schemas :as s]))

(defn create-token!
  {:malli/schema [:=> [:cat
                       [:map [:auth [:map [:jwt-secret :string]]]]
                       c/UserOut]
                  [:map [:token :string]]]}
  [env user-payload]
  (let [clean-user (l/internal->external user-payload)]
    {:token (jwt/sign clean-user (get-in env [:auth :jwt-secret]))}))

(defn hash-password! [password-input]
  {:malli/schema [:=> [:cat :string] :string]}
  (bh/encrypt password-input))

(defn compare-passwords! [password-input user-password]
  {:malli/schema [:=> [:cat :string :string] :boolean]}
  (bh/check password-input user-password))

(defn check-user-existence! [user-input db]
  (db/get-user-by-email! (:email user-input) db))

(defn create-user!
  {:malli/schema [:=> [:cat c/UserData :any] c/UserOut]}
  [new-user db]
  (let [id (shared/generate-uuid!)
        created_at (shared/get-current-inst!)
        hashed-pass (hash-password! (:password new-user))]
    (-> new-user
        (l/user-creation id created_at hashed-pass)
        (db/create-user! db)
        shared/tempid->eid
        (db/get-user! db)
        l/internal->external)))

(defn match-user-input-password! [existing-user user-input]
  {:matches?
   (if (empty? existing-user)
     false
     (compare-passwords! (:password user-input)
                         (:user/password (first (flatten existing-user)))))
   :existing-user (first (flatten existing-user))})

(defn check-credentials!
  {:malli/schema [:=> [:cat c/UserLoginInput :any] s/CredentialsCheck]}
  [user-input db]
  (-> user-input
      (check-user-existence! db)
      (match-user-input-password! user-input)))

(defn gen-token!
  {:malli/schema [:=> [:cat s/CredentialsCheck :any] s/TokenOut]}
  [{:keys [matches? existing-user]} env]
  (if matches?
    (create-token! env existing-user)
    nil))
