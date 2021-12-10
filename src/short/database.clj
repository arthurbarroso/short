(ns short.database
  (:require [datahike-postgres.core]
            [datahike.api :as d]))

(defn create-config [{:keys [backend id user password host port dbname]}]
  (if (= backend "mem")
    {:store {backend :mem :id id}}
    {:store {:backend :pg
             :host host
             :port port
             :username user
             :password password
             :path (str "/" dbname)}}))

(def schema [;;user/uuid
             {:db/ident :user/uuid
              :db/valueType :db.type/uuid
              :db/unique :db.unique/identity
              :db/cardinality :db.cardinality/one}
             ;;user/email
             {:db/ident :user/email
              :db/unique :db.unique/value
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one}
             ;;user/active
             {:db/ident :user/active
              :db/valueType :db.type/boolean
              :db/cardinality :db.cardinality/one}
             ;;user/password
             {:db/ident :user/password
              :db/valueType :db.type/string
              :db/cardinality :db.cardinality/one}
             ;;user/created_at
             {:db/ident :user/created_at
              :db/valueType :db.type/instant
              :db/cardinality :db.cardinality/one}])

(defn create-database [config]
  (d/create-database config))

(defn create-connection [environment]
  (let [config (create-config environment)]
    (when-not (d/database-exists? config)
      (create-database config))
    (let [connection (d/connect config)]
      (d/transact connection schema)
      connection)))
