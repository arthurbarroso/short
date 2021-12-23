(ns short.backoffice.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfa]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_]
   {:email nil
    :password nil
    :authenticated? false
    :token nil
    :current-route nil}))

(re-frame/reg-event-fx
 ::navigate
 (fn [_ [_ route params query]]
   {::navigate! [route params query]}))

(re-frame/reg-fx
 ::navigate!
 (fn [[route params query]]
   (rfa/push-state route params query)))

(re-frame/reg-event-db
 ::navigated
 (fn [db [_ new-match]]
   (let [old-match (:current-route db)
         controllers (rfc/apply-controllers (:controllers old-match) new-match)]
     (assoc db :current-route (assoc new-match :controllers controllers)))))

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
   {:db (assoc db :loading true)
    :http-xhrio {:method :post
                 :uri (str "http://localhost:4000/v1/users/login")
                 :format (ajax/json-request-format)
                 :timeout 8000
                 :params credentials
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::login-success]
                 :on-failure [::login-failure]}}))

(re-frame/reg-event-fx
 ::login-success
 (fn [{:keys [db]} [_ response]]
   {:db (assoc db
               :loading false
               :token (:token response)
               :authenticated? true)
    ::navigate! [:panel]}))

(re-frame/reg-event-fx
 ::login-failure
 (fn [{:keys [db]} [_ response]]
   (cljs.pprint/pprint response)
   {:db (assoc db :loading false)}))
