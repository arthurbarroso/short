(ns short.backoffice.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::email-input
 (fn [db]
   (:email db)))

(re-frame/reg-sub
 ::password-input
 (fn [db]
   (:password db)))

(re-frame/reg-sub
 ::current-route
 (fn [db]
   (:current-route db)))

(re-frame/reg-sub
 ::is-authenticated?
 (fn [db]
   (:authenticated? db)))

(re-frame/reg-sub
 ::products
 (fn [db]
   (:products db)))

(re-frame/reg-sub
 ::product-form-values
 (fn [db]
   (get-in db [:forms :product-form])))
