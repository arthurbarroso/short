(ns short.server
  (:require [short.router :as router]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [short.database :as database]
            [taoensso.timbre :as timbre :refer [info]]
            [aero.core :as aero]
            [clojure.java.io :as io])
  (:gen-class))

(defn app
  [environment]
  (router/routes environment))

(defmethod ig/prep-key :server/jetty
  [_ config]
  (merge config {:port 4000}))

(defmethod ig/init-key :server/jetty
  [_ {:keys [handler port]}]
  (info (str "\nserver running on port: " port))
  (jetty/run-jetty handler {:port port :join? false}))

(defmethod ig/init-key :short/app
  [_ config]
  (info "\nstarted application")
  (app config))

(defmethod ig/init-key :db/postgres
  [_ config]
  (info "\nconfigured db")
  (database/create-connection config))

(defmethod ig/halt-key! :server/jetty
  [_ jetty]
  (.stop jetty))

(defn -main []
  (let [env-vars (aero/read-config (io/resource "config.edn"))
        config-map
        {:server/jetty {:handler (ig/ref :brundij/app)
                        :port (:port env-vars)}
         :short/app {:database (ig/ref :db/postgres)}
         :db/postgres {:host (:database_host env-vars)
                       :port (:database_port env-vars)
                       :user (:database_user env-vars)
                       :backend (:database_backend env-vars)
                       :id (:database_id env-vars)
                       :password (:database_password env-vars)
                       :dbname (:database_name env-vars)}}]
    (-> config-map ig/prep ig/init)))
