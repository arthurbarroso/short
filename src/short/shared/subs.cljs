(ns short.shared.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::current-route
 (fn [db]
   (:current-route db)))

(re-frame/reg-sub
 ::is-authenticated?
 (fn [db]
   (:authenticated? db)))

(re-frame/reg-sub
 ::loading?
 (fn [db]
   (:loading db)))
