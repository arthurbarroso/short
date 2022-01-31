(ns short.users.client.events
  (:require [re-frame.core :as re-frame]
            [short.backoffice.events :as common-events]))

(re-frame/reg-event-db
 ::change-email-input
 (fn [db [_ new-val]]
   (assoc db :email new-val)))

(re-frame/reg-event-db
 ::change-password-input
 (fn [db [_ new-val]]
   (assoc db :password new-val)))

(re-frame/reg-event-fx
 ::login
 (fn [{:keys [db]} [_ credentials]]
   (common-events/build-http-request
    {:db db
     :method :post
     :uri "http://localhost:4000/v1/users/login"
     :params credentials
     :authenticated? true
     :on-success [::login-success]
     :on-failure [::common-events/http-failure]})))

(re-frame/reg-event-fx
 ::login-success
 (fn [{:keys [db]} [_ response]]
   {:db (assoc db
               :loading false
               :token (:token response)
               :authenticated? true)
    ::common-events/set-cookie! {:key "token"
                                 :value (:token response)}
    ::common-events/set-session-storage! {:key "authenticated?"
                                          :value true}
    ::common-events/navigate! [:panel]}))
