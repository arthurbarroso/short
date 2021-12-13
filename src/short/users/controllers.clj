(ns short.users.controllers
  (:require [short.users.handlers :as h]
            [ring.util.response :as rr]))

(defn create-user-controller! [database]
  (fn [request]
    (let [{:keys [email password password-confirmation]}
          (-> request :parameters :body)
          existing-user? (h/check-user-existence!
                          {:email email}
                          database)]
      (if (empty? existing-user?)
        (rr/created ""
                    (h/create-user!
                     {:email email
                      :password password
                      :password-confirmation password-confirmation}
                     database))
        (rr/bad-request {:error "Something went wrong"})))))
