(ns util.test-system
  (:require [integrant.repl :as ig-repl]
            [short.server]
            [integrant.core :as ig]
            [taoensso.timbre :as timbre]
            [clojure.java.io :as io]
            [aero.core :as aero]))

(def environment-vars
  (aero/read-config (io/resource "config.edn") {:profile :test}))

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
