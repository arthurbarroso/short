(ns short.products.ui.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::products
 (fn [db]
   (:products db)))

(re-frame/reg-sub
 ::product-form-values
 (fn [db]
   (get-in db [:forms :product-form])))
