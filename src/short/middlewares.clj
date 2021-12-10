(ns short.middlewares
  (:require [reitit.ring.middleware.exception :as exception]))

(defn exception-handler [message exception request]
  {:status 500
   :body {:message message
          :excepetion (.getClass exception)
          :data (ex-data exception)
          :uri (:uri request)}})

(def exception-middleware
  (exception/create-exception-middleware
   (merge
    exception/default-handlers
    {::error (partial exception-handler "error")
     ::exception (partial exception-handler "exception")
     java.sql.SQLException (partial exception-handler "exception")})))
