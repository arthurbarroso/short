(ns short.middlewares
  (:require [reitit.ring.middleware.exception :as exception]
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [buddy.auth.backends :as backends]
            [buddy.sign.jwt :as jwt]))

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

(defn unsign-jwt! [token secret]
  (try
    (jwt/unsign token secret)
    (catch clojure.lang.ExceptionInfo _e
      nil)))

(defn extract-jwt-token! [request]
  (let [token (-> request :cookies (get "token"))]
    (if token
      (:value token)
      nil)))

(defn unsign-and-identify [request token secret handler]
  (let [unsigned (unsign-jwt! token secret)]
    (if (nil? unsigned)
      {:status 401 :error "Unauthorized"}
      (handler (assoc request :identity unsigned)))))

(defn jws-middleware [handler environment]
  (fn [request]
    (let [secret (get-in environment [:auth :jwt-secret])
          token (extract-jwt-token! request)]
      (if (nil? token)
        {:status 401 :error "Unauthorized"}
        (unsign-and-identify request token secret handler)))))

(defn wrap-jwt-auth [handler environment]
  (wrap-authentication
   handler
   (backends/jws {:secret (get-in environment [:auth :jwt-secret])})))

(defn auth-middleware [handler]
  (fn [request]
    (if (authenticated? request)
      (handler request)
      {:status 401 :body {:error "Unauthorized"}})))
