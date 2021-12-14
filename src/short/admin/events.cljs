(ns short.admin.events
  (:require [re-frame.core :as re-frame]))

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
