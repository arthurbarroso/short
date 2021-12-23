(ns short.backoffice.events
  (:require [re-frame.core :as re-frame]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_]
   {:email nil
    :password nil
    :authenticated? false}))

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
                 :uri (str "http://locallhost:4000/v1/users/login")
                 :format (ajax/json-request-format)
                 :timeout 8000
                 :params credentials
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::login-success]
                 :on-failure [::login-failure]}}))

(re-frame/reg-event-fx
 ::login-success
 (fn [{:keys [db]} [_ response]]
   (cljs.pprint/pprint response)
   {:db (assoc db :loading false)}))

(re-frame/reg-event-fx
 ::login-failure
 (fn [{:keys [db]} [_ response]]
   (cljs.pprint/pprint response)
   {:db (assoc db :loading false)}))
