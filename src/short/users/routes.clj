(ns short.users.routes
  (:require [short.users.controllers :as c]))

(defn routes [environment]
  (let [database (:database environment)]
    ["/users"
     [""
      {:post {:handler (c/create-user-controller! database)
              :parameters {:body {:email string?
                                  :password string?
                                  :password-confirmation string?}}}}]]))
