(ns short.middlewares
  (:require [reitit.ring.middleware.exception :as exception]
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [buddy.auth.backends :as backends]))

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

(defn wrap-jwt-auth [handler environment]
  (wrap-authentication
   handler
   (backends/jws {:secret (get-in environment [:auth :jwt-secret])})))

(defn auth-middleware [handler]
  (fn [request]
    (if (authenticated? request)
      (handler request)
      {:status 401 :body {:error "Unauthorized"}})))
