(ns user
  (:require [short.server]
            [integrant.core :as ig]
            [integrant.repl :as ig-repl]
            [integrant.repl.state :as state]))

(def environment-vars
  {:port 4000
   :database_host "localhost"
   :database_port 5432
   :database_user "postgres"
   :database_backend :pg
   :database_password "postgres"
   :database_name "short"})

(def config-map
  {:server/jetty {:handler (ig/ref :short/app)
                  :port (:port environment-vars)}
   :short/app {:database (ig/ref :db/postgres)}
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
  (go)
  (stop))
