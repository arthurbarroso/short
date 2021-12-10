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
