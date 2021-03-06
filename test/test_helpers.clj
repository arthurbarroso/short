(ns test-helpers
  (:require [short.database :refer [schema]]
            [datahike.api :as d]
            [integrant.repl.state :as state]
            [muuntaja.core :as m]
            [ring.mock.request :as mock]))

(defn create-in-memory-db [id]
  (let [config {:store {:backend :mem
                        :id id}}
        _ (d/create-database config)
        connection (d/connect config)]
    (d/transact connection schema)
    connection))

(defn endpoint-test
  ([method uri]
   (endpoint-test method uri {}))
  ([method uri opts]
   (let [app (-> state/system :short/app)
         request
         (app (-> (mock/request method uri)
                  (cond-> (:auth opts) (mock/cookie :token (:token (:auth opts)))
                          (:body opts) (mock/json-body (:body opts)))))]
     (if (:html opts)
       request
       (update request :body (partial m/decode "application/json"))))))

(defn database-atom []
  (-> state/system :db/postgres))
