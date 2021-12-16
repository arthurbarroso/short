(ns util.test-system
  (:require [integrant.repl :as ig-repl]
            [short.server]
            [integrant.core :as ig]
            [taoensso.timbre :as timbre]))

(def environment-vars
  {:port 4000
   :database_host ""
   :database_port ""
   :database_user ""
   :database_backend "mem"
   :database_id "xis"
   :database_password ""
   :database_name ""})

(def config-map
  {:server/jetty {:handler (ig/ref :short/app)
                  :port (:port environment-vars)}
   :short/app {:database (ig/ref :db/postgres)
               :auth {:jwt-secret "test"}}
   :db/postgres {:host (:database_host environment-vars)
                 :port (:database_port environment-vars)
                 :user (:database_user environment-vars)
                 :backend (:database_backend environment-vars)
                 :id (:database_id environment-vars)
                 :password (:database_password environment-vars)
                 :dbname (:database_name environment-vars)}})

(ig-repl/set-prep!
 (fn [] config-map))

(def reset-all ig-repl/reset-all)

(defn start-fixture [config]
  (timbre/set-level! :error)
  (reset-all)
  config)
