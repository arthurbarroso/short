(ns short.users.client.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::email-input
 (fn [db]
   (:email db)))

(re-frame/reg-sub
 ::password-input
 (fn [db]
   (:password db)))
