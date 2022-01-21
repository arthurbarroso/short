(ns user
  (:require [short.server]
            [integrant.core :as ig]
            [integrant.repl :as ig-repl]
            [integrant.repl.state :as state]
            [aero.core :refer [read-config]]
            [clojure.java.io :as io]))

(def environment-vars
  (read-config (io/resource "config.edn") {:profile :dev}))

(def config-map
  {:server/jetty {:handler (ig/ref :short/app)
                  :port (:port environment-vars)}
   :short/app {:database (ig/ref :db/postgres)
               :auth {:jwt-secret (:jwt_secret environment-vars)}
               :s3 {:bucket (:s3_bucket environment-vars)
                    :creds {:access-key (:access_key environment-vars)
                            :endpoint (:aws_endpoint environment-vars)
                            :secret-key (:secret_key environment-vars)}}}
   :db/postgres {:host (:database_host environment-vars)
                 :port (:database_port environment-vars)
                 :user (:database_user environment-vars)
                 :backend (:database_backend environment-vars)
                 :id (:database_id environment-vars)
                 :password (:database_password environment-vars)
                 :dbname (:database_name environment-vars)}})

(ig-repl/set-prep!
 (fn [] config-map))

(def db (-> state/system :db/postgres))
(def app (-> state/system :short/app))
(def go ig-repl/go)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)
(def stop ig-repl/halt)

(comment
  (require '[malli.dev :as dev])
  (require '[malli.dev.pretty :as pretty])
  (dev/start! {:report (pretty/reporter)})
  (reset-all)
  (stop)
  (go)
  (println environment-vars))
