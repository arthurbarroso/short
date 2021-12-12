(ns short.users.db
  (:require [datahike.api :as d]
            [short.users.schemas :as s]))

(defn create-user!
  {:malli/schema [:=> [:cat s/User :any] s/UserTx]}
  [user db]
  (d/transact db [(assoc user :db/id -1)]))

(defn get-user!
  {:malli/schema [:=> [:cat int? :any] s/ExistingUser]}
  [eid db]
  (d/pull @db '[*] eid))

(defn get-user-by-uuid!
  {:malli/schema [:=> [:cat uuid? :any] s/ExistingUser]}
  [uuid db]
  (d/pull @db '[*] [:user/uuid uuid]))

(defn get-user-by-email!
  {:malli/schema [:=> [:cat string? :any] s/ExistingUser]}
  [email db]
  (d/pull @db '[*] [:user/email email]))
