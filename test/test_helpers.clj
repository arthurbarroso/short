(ns test-helpers
  (:require [short.database :refer [schema]]
            [datahike.api :as d]))

(defn create-in-memory-db [id]
  (let [config {:store {:backend :mem
                        :id id}}
        _ (d/create-database config)
        connection (d/connect config)]
    (d/transact connection schema)
    connection))
