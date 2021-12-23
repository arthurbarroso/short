(ns short.users.logic
  (:require [short.users.schemas :as s]
            [short.users.contracts :as c]
            [short.shared :as shared]))

(defn user-creation
  {:malli/schema [:=> [:cat c/UserData uuid? inst? string?] s/User]}
  [{:keys [email]} id created_at password]
  {:user/email email
   :user/password password
   :user/uuid id
   :user/active true
   :user/created_at created_at})

(defn internal->external
  {:malli/schema [:=> [:cat s/ExistingUser] c/UserOut]}
  [user]
  (dissoc user
          :user/password
          :db/id
          :user/active))
