(ns short.database
  (:require [datahike-postgres.core]
            [datahike.api :as d]
            [short.users.schemas :as users]
            [short.products.schemas :as products]
            [short.variants.schemas :as variants]))

(defn create-config [{:keys [backend id user password host port dbname]}]
  (if (= backend "mem")
    {:store {backend :mem :id id}}
    {:store {:backend :pg
             :host host
             :port port
             :username user
             :password password
             :path (str "/" dbname)}}))

(def schema
  (-> users/user-db-schema
      (conj products/product-db-schema
            variants/variant-db-schema)
      flatten))

(defn create-database [config]
  (d/create-database config))

(defn create-connection [environment]
  (let [config (create-config environment)]
    (when-not (d/database-exists? config)
      (create-database config))
    (let [connection (d/connect config)]
      (d/transact connection schema)
      connection)))
