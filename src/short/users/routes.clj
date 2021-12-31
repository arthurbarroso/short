(ns short.users.routes
  (:require [short.users.controllers :as co]
            [short.users.contracts :as c]))

(defn routes [environment]
  (let [database (:database environment)]
    ["/users"
     [""
      {:post {:handler (co/create-user-controller! database)
              :parameters {:body c/UserData}
              :responses {201 c/UserOut}}}]
     ["/login"
      {:post {:handler (co/login-controller! environment)
              :parameters {:body c/UserLoginInput}
              :responses {200 c/TokenOutResponse}}}]]))
